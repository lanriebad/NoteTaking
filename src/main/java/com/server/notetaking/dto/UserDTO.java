package com.server.notetaking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private Long roleId;

    private String email;

    private String username;

    private Date created;

    private String password;
    
    private String name;


    private String surname;
}
