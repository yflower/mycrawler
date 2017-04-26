package com.jal.crawler.web.data.model.task;

import java.time.LocalDateTime;

/**
 * Created by jianganlan on 2017/4/10.
 */
public class TaskStatusModel {
    String taskTag;

    TaskStatistics taskStatistics;

    public TaskStatusModel(String taskTag, TaskStatistics taskStatistics) {
        this.taskTag = taskTag;
        this.taskStatistics = taskStatistics;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public TaskStatistics getTaskStatistics() {
        return taskStatistics;
    }

    public static class TaskStatistics {
        private String status;
        private int resourceTotal;
        private int processorTotal;
        private int persistTotal;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getResourceTotal() {
            return resourceTotal;
        }

        public void setResourceTotal(int resourceTotal) {
            this.resourceTotal = resourceTotal;
        }

        public int getProcessorTotal() {
            return processorTotal;
        }

        public void setProcessorTotal(int processorTotal) {
            this.processorTotal = processorTotal;
        }

        public int getPersistTotal() {
            return persistTotal;
        }

        public void setPersistTotal(int persistTotal) {
            this.persistTotal = persistTotal;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
    }
}
