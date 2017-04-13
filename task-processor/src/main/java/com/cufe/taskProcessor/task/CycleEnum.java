package com.cufe.taskProcessor.task;

/**
 * Created by jianganlan on 2017/3/26.
 */
public enum CycleEnum {
    RESOURCE_TEST_LIMIT(1000),

    PROCESSOR_ERROR_LIMIT(5),

    RESOURCE_NOT_FOUND_LIMIT(10000),

    SINGLETON_CYCLE_LIMIT(30);

    CycleEnum(int cycle) {
        this.cycle = cycle;
    }

    private int cycle;

    public int getCycle() {
        return cycle;
    }

}
