package com.cufe.taskProcessor.task;

import java.util.concurrent.TimeUnit;

/**
 * Created by jianganlan on 2017/3/26.
 */
public enum CycleEnum {
    RESOURCE_TEST_LIMIT(1000,1,TimeUnit.MINUTES),

    PROCESSOR_ERROR_LIMIT(10,1,TimeUnit.MINUTES),

    RESOURCE_NOT_FOUND_LIMIT(10000,1,TimeUnit.MINUTES),

    PERSIST_ERROR_LIMIT(10,1,TimeUnit.MINUTES),

    SINGLETON_CYCLE_LIMIT(30,1,TimeUnit.MINUTES);


    CycleEnum(int cycle, int time, TimeUnit timeUnit) {
        this.cycle = cycle;
        this.time = time;
        this.timeUnit = timeUnit;
    }


    private int cycle;

    private int time;

    private TimeUnit timeUnit;

    public int getCycle() {
        return cycle;
    }

    public int getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
