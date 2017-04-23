package com.cufe.taskProcessor.task;

/**
 * Created by jal on 2017/2/26.
 */
public enum StatusEnum {
    NO_INIT(0),
    INIT(1),
    STARTED(2),
    STOPPED(3),
    FINISHED(4),
    DESTROYED(5),
    DISCONNECTION(6),
    UNRECOGNIZED(-1),;


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


    public static boolean isStart(StatusEnum statusEnum){
        return statusEnum.getCode()>=2;
    }

    public static StatusEnum numberOf(int status){
        for(StatusEnum statusEnum:StatusEnum.values()){
            if(statusEnum.getCode()==status){
                return statusEnum;
            }
        }
        return UNRECOGNIZED;
    }
}
