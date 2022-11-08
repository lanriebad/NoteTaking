package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RefreshTokenRequest implements Serializable {

    private String accessToken;

    private String refreshToken;
}
