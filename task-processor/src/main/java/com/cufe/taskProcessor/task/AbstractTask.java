package com.cufe.taskProcessor.task;

import java.time.LocalDateTime;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractTask {
    private boolean test;
    private String taskTag;
    private StatusEnum status;

    private TaskStatistics taskStatistics = new TaskStatistics();

    protected AbstractTask() {
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
        LocalDateTime now = LocalDateTime.now();
        this.taskStatistics.getHistoryStatus().put(now, status);
        if (status == StatusEnum.STARTED) {
            this.taskStatistics.setBeginTime(now);
        } else if (status == StatusEnum.FINISHED || status == StatusEnum.DESTROYED) {
            this.taskStatistics.setEndTime(now);
        }
    }

    public TaskStatistics getTaskStatistics() {
        return taskStatistics;
    }

    public abstract void init();


    public void resourceNotFoundHook() {
        taskStatistics.getResourceNotFoundCycle().accumulate(1);
    }

    public void resourceFoundHook() {
        taskStatistics.getResourceFountCycle().accumulate(1);
    }

    public void processorSuccessHook() {
        taskStatistics.getProcessorSuccessCycle().accumulate(1);
    }

    public void processorErrorHook() {
        taskStatistics.getProcessorErrorCycle().accumulate(1);
    }

    public void persisSuccessHook() {
        taskStatistics.getPersistSuccessCycle().accumulate(1);
    }

    public void persistErrorHook() {
        taskStatistics.getPersistErrorCycle().accumulate(1);
    }


    public void setTaskStatistics(TaskStatistics taskStatistics) {
        this.taskStatistics = taskStatistics;
    }
}
