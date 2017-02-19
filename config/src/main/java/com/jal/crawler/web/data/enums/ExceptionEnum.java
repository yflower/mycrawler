package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/2/19.
 */
public enum ExceptionEnum {
    PARAM_ERROR("500", "param error"),
    DB_CONFIG_ERROR("500", "db config error"),
    ADDRESS_NOT_FOUND("404", "address not found"),
    COMPONENT_NOT_FOUND("404", "component not found");


    private String code;

    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
