package com.jal.crawler.enums;

/**
 * Created by jal on 2017/2/26.
 */
public enum StatusEnum {
    NO_INIT(0),
    INIT(1),
    STARTED(2),
    STOPPED(3),
    FINISHED(4),
    DESTROYED(5);


    private int code;

    StatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
