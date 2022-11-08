package com.server.notetaking.dto;

import com.server.notetaking.model.Label;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NoteResponse implements Serializable {

    private Long Id;
    private String title;
    private String content;

}
