package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/2/25.
 */
public enum TaskOperationEnum {
    ADD(0),
    STOP(1),
    UPDATE(2),
    FINISH(3),
    DESTROY(4),
    RESTART(5),
    UNRECOGNIZED(-1),;


    private int code;

    TaskOperationEnum(int code) {
        this.code = code;
    }

    public static TaskOperationEnum numberOf(int code) {
        switch (code) {
            case 1:
                return ADD;
            case 2:
                return STOP;
            case 3:
                return UPDATE;
            case 4:
                return FINISH;
            case 5:
                return DESTROY;
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }
}
