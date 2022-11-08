package com.server.notetaking.controller;

import com.server.notetaking.dto.*;
import com.server.notetaking.model.User;
import com.server.notetaking.service.UserService;
import com.server.notetaking.utils.CryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    CryptUtils cryptUtils;

    @PostMapping(value = "/register")
    public LoginResponse registerUser(
            @Valid @RequestBody UserDTO request, @RequestHeader(name = "x-app-id", required = true) String appId) {
        LoginResponse defaultServiceResponse = new LoginResponse();
        ServiceResponse.ResponseCode responseCode = ServiceResponse.ResponseCode.USER_ALREADY_EXIST;
        if (StringUtils.isNotEmpty(request.getName()) && StringUtils.isNotEmpty(request.getSurname())) {
            Optional<User> existingUser = userService.findIfUsernameAndAppIdExists(request.getUsername().toLowerCase(), appId);
            if (existingUser.isPresent()) {
                defaultServiceResponse.setResponseCode(responseCode.USER_ALREADY_EXIST.getCode());
                defaultServiceResponse.setResponseMsg(responseCode.USER_ALREADY_EXIST.getDefaultMessage());
            } else {
                defaultServiceResponse = userService.registerUser(request, appId);
            }
        } else {
            defaultServiceResponse.setResponseCode(responseCode.BAD_REQUEST.getCode());
            defaultServiceResponse.setResponseMsg(responseCode.BAD_REQUEST.getDefaultMessage());
        }
        return defaultServiceResponse;
    }


    @PostMapping(value = "/login")
    @Transactional
    public LoginResponse login(
            @Valid @RequestBody LoginRequest login, @RequestHeader(name = "x-app-id", required = true) String appId) {
        LoginResponse loginResponse = new LoginResponse();
        String decryptPassword = StringUtils.EMPTY;
        String username = login.getUsername().toLowerCase();
        decryptPassword = cryptUtils.decrypt(login.getPassword());
        ServiceResponse.ResponseCode responseCode = ServiceResponse.ResponseCode.INVALID_CREDENTIALS;
        if (username == null || decryptPassword == null) {
            loginResponse.setResponseMsg(responseCode.INVALID_CREDENTIALS.getDefaultMessage());
            loginResponse.setResponseCode(responseCode.INVALID_CREDENTIALS.getCode());
            return loginResponse;
        }
        loginResponse = userService.login(username, appId, decryptPassword);
        return loginResponse;
    }

    @PostMapping(value = "/refresh")
    public LoginResponse refresh(@Valid @RequestBody RefreshTokenRequest
                                         refreshTokenRequest, @RequestHeader(name = "x-app-id", required = true) String appId)
            throws Exception {
        LoginResponse loginResponse = null;
        ServiceResponse.ResponseCode responseCode = ServiceResponse.ResponseCode.INVALID_CREDENTIALS;

        LoginDTO login = userService.findByAppIdAndRefreshToken(appId, refreshTokenRequest.getRefreshToken());
        if (login == null) {
            loginResponse.setResponseMsg(responseCode.INVALID_CREDENTIALS.getDefaultMessage());
            loginResponse.setResponseCode(responseCode.INVALID_CREDENTIALS.getCode());
        } else {
            loginResponse = userService.getLoginResponse(login);
            loginResponse.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
            loginResponse.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());
        }
        return loginResponse;
    }
}
