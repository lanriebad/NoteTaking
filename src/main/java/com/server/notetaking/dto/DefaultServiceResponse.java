package com.server.notetaking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultServiceResponse<T extends Serializable> implements ServiceResponse  {

    private String appVersionCode;

    protected String responseCode;

    protected List<T> responseData;

    protected String responseMsg;


    public DefaultServiceResponse() {
        super();
        responseData = new ArrayList<T>();
    }


    public DefaultServiceResponse(String responseCode, String responseMsg, List<T> responseData) {
        this.setResponseCode(responseCode);
        this.setResponseMsg(responseMsg);
        this.setResponseData(responseData);
    }


    public String getAppVersionCode() {
        return appVersionCode;
    }


    @Override
    public String getResponseCode() {
        return responseCode;
    }


    public List<T> getResponseData() {
        return responseData;
    }


    @Override
    public String getResponseMsg() {
        return responseMsg;
    }


    @JsonIgnore
    public boolean isError() {
        return ResponseCode.ERROR.equals(responseCode);
    }


    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }


    @Override
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }


    public void setResponseData(List<T> responseData) {
        this.responseData = responseData;
    }


    @Override
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
