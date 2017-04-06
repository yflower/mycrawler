package com.cufe.taskProcessor.enums;

/**
 * Created by jianganlan on 2017/4/3.
 */
public enum TaskTypeEnum {
    ADD(0),
    STOP(1),
    UPDATE(2),
    FINISH(3),
    DESTROY(4),
    UNRECOGNIZED(-1),;
    private int code;

    TaskTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskTypeEnum numberOf(int code){
        for(TaskTypeEnum typeEnum:TaskTypeEnum.values()){
            if(typeEnum.getCode()==code){
                return typeEnum;
            }
        }
        return UNRECOGNIZED;
    }
}
