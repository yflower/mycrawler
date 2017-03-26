package com.jal.crawler.task;

import com.jal.crawler.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by jianganlan on 2017/3/26.
 */
public class TaskStatistics {
    //任务时间相关
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    //任务执行周期次数
    private int curCycle;
    private int successCycle;
    private int failedCycle;
    //平均花费时间
    private int cyclePerTime;

    //任务状态变更记录
    private Map<LocalDateTime,StatusEnum> historyStatus;

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

    public int getCurCycle() {
        return curCycle;
    }

    public void setCurCycle(int curCycle) {
        this.curCycle = curCycle;
    }

    public int getSuccessCycle() {
        return successCycle;
    }

    public void setSuccessCycle(int successCycle) {
        this.successCycle = successCycle;
    }

    public int getFailedCycle() {
        return failedCycle;
    }

    public void setFailedCycle(int failedCycle) {
        this.failedCycle = failedCycle;
    }

    public int getCyclePerTime() {
        return cyclePerTime;
    }

    public void setCyclePerTime(int cyclePerTime) {
        this.cyclePerTime = cyclePerTime;
    }

    public Map<LocalDateTime, StatusEnum> getHistoryStatus() {
        return historyStatus;
    }

    public void setHistoryStatus(Map<LocalDateTime, StatusEnum> historyStatus) {
        this.historyStatus = historyStatus;
    }
}
