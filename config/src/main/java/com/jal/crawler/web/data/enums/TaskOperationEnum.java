package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/2/25.
 */
public enum TaskOperationEnum {
    ADD(1),
    STOP(2),
    UPDATE(3),
    FINISH(4),
    DESTROY(5);


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
}
