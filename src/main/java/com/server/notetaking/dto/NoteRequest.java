package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoteRequest implements Serializable {

    private Long id;
    private String title;

    private String content;

    private String email;
}
