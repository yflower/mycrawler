package com.jal.crawler.task;

import com.jal.crawler.enums.StatusEnum;

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

    private LongAccumulator urlTotalCycle = new LongAccumulator(function, 0);

    private LongAccumulator urlFoundCycle = new LongAccumulator(function, 0);

    private LongAccumulator urlNotFoundCycle = new LongAccumulator(function, 0);

    private LongAccumulator downloadTotalCycle = new LongAccumulator(function, 0);

    private LongAccumulator downloadErrorCycle = new LongAccumulator(function, 0);

    private LongAccumulator downloadSuccessCycle = new LongAccumulator(function, 0);

    private LongAccumulator persistTotalCycle = new LongAccumulator(function, 0);

    private LongAccumulator persistSuccessCycle = new LongAccumulator(function, 0);

    private LongAccumulator persistErrorCycle = new LongAccumulator(function, 0);


    public TaskStatistics() {
        beginTime=LocalDateTime.now();
        historyStatus=new HashMap<>();
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


    public LongAccumulator getUrlTotalCycle() {
        return urlTotalCycle;
    }


    public LongAccumulator getUrlFoundCycle() {
        return urlFoundCycle;
    }


    public LongAccumulator getUrlNotFoundCycle() {
        return urlNotFoundCycle;
    }


    public LongAccumulator getDownloadTotalCycle() {
        return downloadTotalCycle;
    }


    public LongAccumulator getDownloadErrorCycle() {
        return downloadErrorCycle;
    }

    public LongAccumulator getDownloadSuccessCycle() {
        return downloadSuccessCycle;
    }


    public LongAccumulator getPersistTotalCycle() {
        return persistTotalCycle;
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

    public void setHistoryStatus(Map<LocalDateTime, StatusEnum> historyStatus) {
        this.historyStatus = historyStatus;
    }
}
