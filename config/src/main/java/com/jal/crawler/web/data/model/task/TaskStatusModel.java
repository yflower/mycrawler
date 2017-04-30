package com.jal.crawler.web.data.model.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jal.crawler.web.data.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by jianganlan on 2017/4/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusModel {
    private boolean test;
    private String taskTag;
    private StatusEnum status;

    private TaskStatistics taskStatistics;


    public String getTaskTag() {
        return taskTag;
    }

    public TaskStatistics getTaskStatistics() {
        return taskStatistics;
    }

    public static class TaskStatistics {

        private LocalDateTime beginTime;
        private LocalDateTime endTime;


        //平均花费时间
        private long cyclePerTime ;

        //任务状态变更记录
        private Map<LocalDateTime, StatusEnum> historyStatus;

        private long resourceTotalCycle;

        private long processorTotalCycle;

        private long persistTotalCycle;

        private long resourceFountCycle;

        private long resourceNotFoundCycle;

        private long processorErrorCycle;

        private long processorSuccessCycle;

        private long persistSuccessCycle;

        private long persistErrorCycle;

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

        public long getCyclePerTime() {
            return cyclePerTime;
        }

        public void setCyclePerTime(long cyclePerTime) {
            this.cyclePerTime = cyclePerTime;
        }

        public Map<LocalDateTime, StatusEnum> getHistoryStatus() {
            return historyStatus;
        }

        public void setHistoryStatus(Map<LocalDateTime, StatusEnum> historyStatus) {
            this.historyStatus = historyStatus;
        }

        public long getResourceTotalCycle() {
            return resourceTotalCycle;
        }

        public void setResourceTotalCycle(long resourceTotalCycle) {
            this.resourceTotalCycle = resourceTotalCycle;
        }

        public long getProcessorTotalCycle() {
            return processorTotalCycle;
        }

        public void setProcessorTotalCycle(long processorTotalCycle) {
            this.processorTotalCycle = processorTotalCycle;
        }

        public long getPersistTotalCycle() {
            return persistTotalCycle;
        }

        public void setPersistTotalCycle(long persistTotalCycle) {
            this.persistTotalCycle = persistTotalCycle;
        }

        public long getResourceFountCycle() {
            return resourceFountCycle;
        }

        public void setResourceFountCycle(long resourceFountCycle) {
            this.resourceFountCycle = resourceFountCycle;
        }

        public long getResourceNotFoundCycle() {
            return resourceNotFoundCycle;
        }

        public void setResourceNotFoundCycle(long resourceNotFoundCycle) {
            this.resourceNotFoundCycle = resourceNotFoundCycle;
        }

        public long getProcessorErrorCycle() {
            return processorErrorCycle;
        }

        public void setProcessorErrorCycle(long processorErrorCycle) {
            this.processorErrorCycle = processorErrorCycle;
        }

        public long getProcessorSuccessCycle() {
            return processorSuccessCycle;
        }

        public void setProcessorSuccessCycle(long processorSuccessCycle) {
            this.processorSuccessCycle = processorSuccessCycle;
        }

        public long getPersistSuccessCycle() {
            return persistSuccessCycle;
        }

        public void setPersistSuccessCycle(long persistSuccessCycle) {
            this.persistSuccessCycle = persistSuccessCycle;
        }

        public long getPersistErrorCycle() {
            return persistErrorCycle;
        }

        public void setPersistErrorCycle(long persistErrorCycle) {
            this.persistErrorCycle = persistErrorCycle;
        }
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void setTaskStatistics(TaskStatistics taskStatistics) {
        this.taskStatistics = taskStatistics;
    }
}
