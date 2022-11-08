package com.server.notetaking.dto;

import com.server.notetaking.model.Note;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LabelRequest implements Serializable {

    private String name;

    private String username;

    List<Note> notes;
}
