package com.jal.crawler.web.data.enums;

/**
 * Created by jal on 2017/2/25.
 */
public enum ComponentEnum {
    DOWNLOAD(0),
    RESOLVE(1);

    private int code;

    ComponentEnum(int code) {
        this.code = code;
    }

    public static ComponentEnum numberOf(int value) {
        for (ComponentEnum componentEnum : ComponentEnum.values()) {
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
