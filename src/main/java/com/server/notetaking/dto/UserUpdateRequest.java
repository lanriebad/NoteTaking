package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserUpdateRequest implements Serializable {

    private String firstName;

    private Long id;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String password;

    private String newPassword;
}
