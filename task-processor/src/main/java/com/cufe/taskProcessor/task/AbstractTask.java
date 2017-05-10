package com.cufe.taskProcessor.task;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractTask {
    private boolean test;
    private String taskTag;
    private StatusEnum status;

    private TaskStatistics taskStatistics = new TaskStatistics();

    private limit limit=new limit();

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
        Optional<LocalDateTime> notFound = this.limit.lastResourceNotFound;
        if(notFound==null||!notFound.isPresent()){
            this.limit.lastResourceNotFound=Optional.of(LocalDateTime.now());
        }
    }

    public void resourceFoundHook() {
        taskStatistics.getResourceFountCycle().accumulate(1);
        this.limit.lastResourceNotFound=Optional.empty();

    }

    public void processorSuccessHook() {
        taskStatistics.getProcessorSuccessCycle().accumulate(1);
        this.limit.lastProcessorError=Optional.empty();


    }

    public void processorErrorHook() {
        taskStatistics.getProcessorErrorCycle().accumulate(1);
        Optional<LocalDateTime> processorError = this.limit.lastProcessorError;
        if(processorError==null||!processorError.isPresent()){
            this.limit.lastProcessorError=Optional.of(LocalDateTime.now());
        }


    }

    public void persisSuccessHook() {
        taskStatistics.getPersistSuccessCycle().accumulate(1);
        this.limit.lastPersistEoor=Optional.empty();


    }

    public void persistErrorHook() {
        taskStatistics.getPersistErrorCycle().accumulate(1);
        Optional<LocalDateTime> persistEoor = this.limit.lastPersistEoor;
        if(persistEoor==null||!persistEoor.isPresent()){
            this.limit.lastPersistEoor=Optional.of(LocalDateTime.now());
        }

    }


    public void setTaskStatistics(TaskStatistics taskStatistics) {
        this.taskStatistics = taskStatistics;
    }


    public static class limit{
        private Optional<LocalDateTime> lastResourceNotFound;
        private Optional<LocalDateTime> lastProcessorError;
        private Optional<LocalDateTime> lastPersistEoor;

        public Optional<LocalDateTime> getLastResourceNotFound() {
            return lastResourceNotFound;
        }

        public void setLastResourceNotFound(Optional<LocalDateTime> lastResourceNotFound) {
            this.lastResourceNotFound = lastResourceNotFound;
        }

        public Optional<LocalDateTime> getLastProcessorError() {
            return lastProcessorError;
        }

        public void setLastProcessorError(Optional<LocalDateTime> lastProcessorError) {
            this.lastProcessorError = lastProcessorError;
        }

        public Optional<LocalDateTime> getLastPersistEoor() {
            return lastPersistEoor;
        }

        public void setLastPersistEoor(Optional<LocalDateTime> lastPersistEoor) {
            this.lastPersistEoor = lastPersistEoor;
        }
    }

    public AbstractTask.limit getLimit() {
        return limit;
    }
}
