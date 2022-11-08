package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabelResponse implements Serializable {

    private String name;

    private Long id;
}
