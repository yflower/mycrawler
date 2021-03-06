package com.cufe.taskProcessor.component.relation;

/**
 * Created by jianganlan on 2017/4/13.
 */
public enum ComponentRelationTypeEnum {
    LEADER(0, "领导"),
    SLAVE(1, "分支组件");

    ComponentRelationTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static ComponentRelationTypeEnum numberOf(int type) {
        for (ComponentRelationTypeEnum typeEnum : ComponentRelationTypeEnum.values()) {
            if (type == typeEnum.code) {
                return typeEnum;
            }
        }
        return SLAVE;
    }
}
