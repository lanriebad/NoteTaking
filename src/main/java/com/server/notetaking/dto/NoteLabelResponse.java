package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class NoteLabelResponse implements Serializable {

    private Long labelId;
    private String title;
    private String content;
    private Long noteId;
    private String labelName;
}
