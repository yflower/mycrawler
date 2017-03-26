package com.jal.crawler.enums;

/**
 * Created by jianganlan on 2017/3/26.
 */
public enum CycleEnum {
    TEST_URL(10),
    URL_NOT_FOUND(100),
    PAGE_DOWN_ERROR(10);

    CycleEnum(int cycle) {
        this.cycle = cycle;
    }

    private int cycle;

    public int getCycle() {
        return cycle;
    }

}
