package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchResponse implements Serializable {

    private String labelName;

    private String content;

    private String title;

    private Long labelId;

    private Long noteId;
}
