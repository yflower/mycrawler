package com.jal.crawler.web.data.enums;

/**
 * Created by jianganlan on 2017/4/13.
 */
public enum ComponentRelationTypeEnum {
    LEADER(0, "领导"),
    CLUSTER(1, "分支组件");

    private int code;
    private String msg;
    ComponentRelationTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ComponentRelationTypeEnum numberOf(int type) {
        for (ComponentRelationTypeEnum typeEnum : ComponentRelationTypeEnum.values()) {
            if (type == typeEnum.code) {
                return typeEnum;
            }
        }
        return CLUSTER;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
