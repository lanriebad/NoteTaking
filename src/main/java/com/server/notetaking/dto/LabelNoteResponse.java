package com.server.notetaking.dto;

import com.server.notetaking.model.Label;
import lombok.Data;

@Data
public class LabelNoteResponse {

    private Long id;
    private String title;
    private String content;
    private Label label;

}
