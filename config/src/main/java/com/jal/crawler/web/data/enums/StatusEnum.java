package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/2/19.
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

    public static StatusEnum numberOf(int code) {
        switch (code) {
            case 0:
                return NO_INIT;
            case 1:
                return INIT;
            case 2:
                return STARTED;
            case 3:
                return STOPPED;
            case 4:
                return FINISHED;
            case 5:
                return DESTROYED;
            default:
                return DESTROYED;
        }
    }


}
