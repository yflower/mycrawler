package com.cufe.taskProcessor.task;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

/**
 * Created by jianganlan on 2017/3/26.
 */
public class TaskStatistics {
    LongBinaryOperator function = (l, r) -> l + r;

    //任务时间相关
    private LocalDateTime beginTime;
    private LocalDateTime endTime;


    //平均花费时间
    private LongAccumulator cyclePerTime = new LongAccumulator(function, 0);

    //任务状态变更记录
    private Map<LocalDateTime, StatusEnum> historyStatus;

    private long resourceTotalCycle;

    private long processorTotalCycle;

    private long persistTotalCycle;

    private LongAccumulator resourceFountCycle = new LongAccumulator(function, 0);

    private LongAccumulator resourceNotFoundCycle = new LongAccumulator(function, 0);

    private LongAccumulator processorErrorCycle = new LongAccumulator(function, 0);

    private LongAccumulator processorSuccessCycle = new LongAccumulator(function, 0);

    private LongAccumulator persistSuccessCycle = new LongAccumulator(function, 0);

    private LongAccumulator persistErrorCycle = new LongAccumulator(function, 0);


    public TaskStatistics() {
        beginTime = LocalDateTime.now();
        historyStatus = new HashMap<>();
        historyStatus.put(beginTime, StatusEnum.NO_INIT);
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LongAccumulator getCyclePerTime() {
        return cyclePerTime;
    }

    public long getResourceTotalCycle() {
        return resourceFountCycle.longValue() + resourceNotFoundCycle.longValue();
    }

    public long getProcessorTotalCycle() {
        return processorErrorCycle.longValue() + processorSuccessCycle.longValue();
    }

    public long getPersistTotalCycle() {
        return persistSuccessCycle.longValue() + persistErrorCycle.longValue();
    }

    public LongAccumulator getResourceFountCycle() {
        return resourceFountCycle;
    }

    public LongAccumulator getResourceNotFoundCycle() {
        return resourceNotFoundCycle;
    }

    public LongAccumulator getProcessorErrorCycle() {
        return processorErrorCycle;
    }

    public LongAccumulator getProcessorSuccessCycle() {
        return processorSuccessCycle;
    }

    public LongAccumulator getPersistSuccessCycle() {
        return persistSuccessCycle;
    }


    public LongAccumulator getPersistErrorCycle() {
        return persistErrorCycle;
    }


    public Map<LocalDateTime, StatusEnum> getHistoryStatus() {
        return historyStatus;
    }

}
