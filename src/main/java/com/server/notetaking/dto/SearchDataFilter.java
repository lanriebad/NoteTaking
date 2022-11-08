package com.server.notetaking.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class SearchDataFilter implements Serializable {

    private String email;

    private String appId;

    private String searchInfo;
}
