package com.server.notetaking.service;

import com.server.notetaking.dto.LoginDTO;
import com.server.notetaking.dto.LoginResponse;
import com.server.notetaking.dto.UserDTO;
import com.server.notetaking.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findIfUsernameAndAppIdExists(String username, String appId);

    LoginResponse registerUser(UserDTO request, String appId);

    LoginResponse login(String username, String appId,String decryptPassword);

    LoginResponse getLoginResponse(LoginDTO login);

    LoginDTO findByAppIdAndRefreshToken(String appId, String refreshToken);
}
