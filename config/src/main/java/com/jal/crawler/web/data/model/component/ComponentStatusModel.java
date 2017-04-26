package com.jal.crawler.web.data.model.component;

import com.jal.crawler.web.data.model.task.TaskStatusModel;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/10.
 */
public class ComponentStatusModel {

    private String status;

    private List<TaskStatusModel> tasks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TaskStatusModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskStatusModel> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "ComponentStatusModel{" +
                "status='" + status + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
