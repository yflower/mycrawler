package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/2/19.
 */
public enum ExceptionEnum {
    PARAM_ERROR("500", "参数错误"),
    DB_CONFIG_ERROR("500", "数据库设置没有指定"),
    ADDRESS_NOT_FOUND("404", "地址不存在"),
    COMPONENT_NOT_FOUND("404", "没有找到组件"),
    CONFIG_ERROR("400", "组件设置出错"),
    UNKNOWN("500", "服务器异常");


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
