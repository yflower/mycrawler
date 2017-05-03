package com.jal.crawler.enums;

/**
 * Created by jianganlan on 2017/5/2.
 */
public enum ComponentTypeEnum {
    DOWNLOAD(0),
    RESOLVE(1),
    DATA(2);

    private int code;

    ComponentTypeEnum(int code) {
        this.code = code;
    }

    public static ComponentTypeEnum numberOf(int value) {
        for (ComponentTypeEnum componentEnum : ComponentTypeEnum.values()) {
            if (value == componentEnum.getCode()) {
                return componentEnum;
            }
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
