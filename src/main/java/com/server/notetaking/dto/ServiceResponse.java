package com.server.notetaking.dto;

import java.io.Serializable;

public interface ServiceResponse<T extends Serializable> extends Serializable {

    enum ResponseCode {
        ERROR("99", "Operation Error"),BLOCKED_PROFILE("52", "Your Profile is blocked."), USER_ALREADY_EXIST("403", "User already exist."), USER_NOT_FOUND("404",
                "User not found"), SERVICE_NOT_AVAILABLE("302", "Service not available"),REQUEST_NOT_FOUND("404", "Request Not Found"),BAD_REQUEST("400", "Surname and Name must not be null"),INVALID_TOKEN("53", "Invalid Token"), SUCCESSFUL("00", "Operation Successful"),INVALID_CREDENTIALS("57",
                "invalid credentials");

        protected String code;

        protected String defaultMessage;


        ResponseCode(String code, String defaultMessage) {
            this.code = code;
            this.defaultMessage = defaultMessage;
        }


        public String getCode() {
            return code;
        }


        public String getDefaultMessage() {
            return defaultMessage;
        }


        @Override
        public String toString() {
            return getCode();
        }
    }


    String getResponseCode();


    String getResponseMsg();


    void setResponseCode(String responseCode);


    void setResponseMsg(String responseMsg);
}
