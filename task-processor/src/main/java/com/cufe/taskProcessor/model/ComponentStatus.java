package com.cufe.taskProcessor.model;

import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.task.AbstractTask;

import java.util.List;
import java.util.Map;

/**
 * Created by jianganlan on 2017/4/3.
 */
public class ComponentStatus {
    private int componentType;

    private StatusEnum componentStatus;

    private List<AbstractTask> tasks;


    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public StatusEnum getComponentStatus() {
        return componentStatus;
    }

    public void setComponentStatus(StatusEnum componentStatus) {
        this.componentStatus = componentStatus;
    }

    public List<AbstractTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<AbstractTask> tasks) {
        this.tasks = tasks;
    }
}
