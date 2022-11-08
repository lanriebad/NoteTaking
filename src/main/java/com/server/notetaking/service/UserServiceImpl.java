package com.server.notetaking.service;

import com.server.notetaking.constant.AuthConstant;
import com.server.notetaking.dto.LoginDTO;
import com.server.notetaking.dto.LoginResponse;
import com.server.notetaking.dto.ServiceResponse;
import com.server.notetaking.dto.UserDTO;
import com.server.notetaking.model.User;
import com.server.notetaking.repository.ApplicationInfoRepository;
import com.server.notetaking.repository.RoleRepository;
import com.server.notetaking.repository.UserRepository;
import com.server.notetaking.utils.CryptUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.server.notetaking.constant.AuthConstant.UNBLOCKED;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${user.timeout.forjwt:1800}")
    String userTimeOutForJwt;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationInfoRepository applicationInfoRepository;

    @Autowired
    private CryptUtils cryptUtils;

    @Value("${jwt.jwtSignKey:NoteTaking123#}")
    private String jwtSignKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findIfUsernameAndAppIdExists(String username, String appId) {
        return userRepository.findIfUsernameAndAppIdisPresent(username, appId);
    }

    @Override
    public LoginResponse registerUser(UserDTO request, String appId) {
        User userInfo = new User();
        LoginResponse loginResponse = new LoginResponse();
        String decryptPassword = cryptUtils.decrypt(request.getPassword());
        String hashedPassword = passwordEncoder.encode(decryptPassword);
        userInfo.setPassword(hashedPassword);
        userInfo.setEmail(request.getEmail().toLowerCase());
        userInfo.setUsername(request.getUsername().toLowerCase());
        userInfo.setName(request.getName());
        userInfo.setRole(roleRepository.findById(request.getRoleId()));
        userInfo.setActiveStatus(AuthConstant.UNBLOCKED);
        userInfo.setApplicationInfo(applicationInfoRepository.findByApplicationId(appId));
        userRepository.saveAndFlush(userInfo);
        LoginDTO foundUserInfo = findByUsernameAndAppId(userInfo.getUsername(), appId);
        if (foundUserInfo.getUser() != null) {
            loginResponse.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
            loginResponse.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.name());
            loginResponse = getLoginResponse(foundUserInfo);
        }
        return loginResponse;

    }

    @Override
    public LoginResponse login(String username, String appId, String decryptPassword) {
        LoginResponse loginResponse = new LoginResponse();
        ServiceResponse.ResponseCode responseCode = ServiceResponse.ResponseCode.INVALID_CREDENTIALS;
        User userInfo = userRepository.findIfUsernameAndAppIdExists(username, appId);
        if (userInfo == null) {
            loginResponse.setResponseMsg(responseCode.USER_NOT_FOUND.getDefaultMessage());
            loginResponse.setResponseCode(responseCode.USER_NOT_FOUND.getCode());
            return loginResponse;
        } else if (userInfo != null && (userInfo.getActiveStatus().equals(AuthConstant.BLOCKED))) {
            loginResponse.setResponseMsg(responseCode.BLOCKED_PROFILE.getDefaultMessage());
            loginResponse.setResponseCode(responseCode.BLOCKED_PROFILE.getCode());
            return loginResponse;
        }
        LoginDTO foundUserInfo = findByUsernameAndAppId(username, appId);
        if (foundUserInfo.getUser() != null) {
            String pwd = foundUserInfo.getUser().getPassword();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(decryptPassword, pwd)) {
                loginResponse = failedLoginAttempt(loginResponse, responseCode, userInfo);
                userRepository.save(userInfo);
            } else {
                loginResponse = successfulLoginResponse(userInfo, foundUserInfo);
            }
        }
        return loginResponse;

    }


    private LoginResponse failedLoginAttempt(LoginResponse loginResponse, ServiceResponse.ResponseCode responseCode, User userInfo) {
        loginResponse.setResponseMsg(responseCode.getDefaultMessage());
        loginResponse.setResponseCode(responseCode.getCode());
        userInfo.setFailedLoginAttempts(userInfo.getFailedLoginAttempts() + 1);
        if (userInfo.getFailedLoginAttempts() > 2) {
            userInfo.setActiveStatus(AuthConstant.BLOCKED);
            loginResponse.setResponseMsg(responseCode.BLOCKED_PROFILE.getDefaultMessage());
            loginResponse.setResponseCode(responseCode.BLOCKED_PROFILE.getCode());
        }
        return loginResponse;
    }

    private LoginResponse successfulLoginResponse(User userInfo, LoginDTO foundUserInfo) {
        LoginResponse loginResponse;
        loginResponse = getLoginResponse(foundUserInfo);
        userInfo.setFailedLoginAttempts(AuthConstant.RESET_LOGIN);
        userInfo.setActiveStatus(UNBLOCKED);
        userInfo.setLastLoginDate(new Date());
        userRepository.save(userInfo);
        loginResponse.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
        loginResponse.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());
        return loginResponse;
    }


    public LoginResponse getLoginResponse(LoginDTO userInfo) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
        loginResponse.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.name());
        List<LoginDTO> responseData = Collections.singletonList(userInfo);
        if (!responseData.isEmpty()) {
            LoginDTO response = responseData.get(0);
            LoginDTO dto = new LoginDTO();
            UserDTO user = new UserDTO();
            dto.setAccessToken(response.getAccessToken());
            dto.setRefreshToken(response.getRefreshToken());
            user.setUsername(response.getUser().getUsername());
            user.setCreated(response.getUser().getCreated());
            user.setRoleId(response.getUser().getRoleId());
            dto.setUser(user);
            loginResponse.setResponseData(Collections.singletonList(dto));
        }
        return loginResponse;
    }

    @Override
    public LoginDTO findByAppIdAndRefreshToken(String appId, String refreshToken) {
        User userInfo = userRepository.findByAppIdAndRefreshToken(appId, refreshToken, new Date());
        return refreshInfoToUserInfoDTO(userInfo);
    }

    private LoginDTO refreshInfoToUserInfoDTO(User userInfo) {
        LoginDTO loginDto = new LoginDTO();
        String accessToken = generateJwt(userInfo);
        loginDto.setRefreshToken(userInfo.getRefreshToken());
        loginDto.setAccessToken(accessToken);
        UserDTO user = new UserDTO();
        loginDto.setUser(user);
        return loginDto;
    }

    public LoginDTO findByUsernameAndAppId(String username, String appId) {
        Optional<User> userInfo = userRepository.findIfUsernameAndAppIdisPresent(username.toLowerCase(), appId);
        if (userInfo.isPresent()) {
            User user = userInfo.get();
            return userInfoToUserInfoDTO(user);
        } else {
            return new LoginDTO();
        }
    }


    public String generateRefreshToken(User userInfo) {
        String pattern = String.format("%s %s %s", userInfo.getEmail(), userInfo.getUsername(), userInfo.getCreated());
        String refreshToken = Base64.getEncoder().encodeToString(pattern.getBytes());
        userInfo.setRefreshToken(refreshToken);
        userInfo.setRefreshTokenExpiry(DateUtils.addDays(new Date(), 7));
        userRepository.save(userInfo);
        return refreshToken;
    }

    private LoginDTO userInfoToUserInfoDTO(User userInfo) {
        LoginDTO loginDTO = new LoginDTO();
        String refreshToken = generateRefreshToken(userInfo);
        String accessToken = generateJwt(userInfo);
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleId(userInfo.getRole().getId());
        userDTO.setEmail(userInfo.getEmail());
        userDTO.setUsername(userInfo.getUsername());
        userDTO.setPassword(userInfo.getPassword());
        loginDTO.setUser(userDTO);
        loginDTO.setAccessToken(accessToken);
        loginDTO.setRefreshToken(refreshToken);
        return loginDTO;
    }

    public String generateJwt(User userInfo) {
        String jwtToken;
        long guestTimeFormat = CryptUtils.computeStringValue(userTimeOutForJwt);
        jwtToken = Jwts.builder().setIssuer(userInfo.getApplicationInfo().getAppId()).setSubject(userInfo.getUsername()).claim("roles", "user")
                .claim("roles", userInfo.getRole().getId()).claim("roles", userInfo.getApplicationInfo().getAppId()).claim("appId", userInfo.getApplicationInfo().getAppId()).claim("username", userInfo.getUsername())
                .claim("email", userInfo.getEmail())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + (guestTimeFormat)))
                .signWith(SignatureAlgorithm.HS256, jwtSignKey).compact();
        return jwtToken;
    }
}
