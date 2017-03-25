package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/3/19.
 */
public enum TaskLoadTypeEnum {
    MAX_SPEED(0,"组件全速");

    private int code;

    private String message;

    TaskLoadTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
