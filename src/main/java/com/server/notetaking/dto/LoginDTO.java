package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String accessToken;

    private String refreshToken;

    private String userLoggedIn;

    private UserDTO user;
}
