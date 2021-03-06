package com.cufe.taskProcessor.component.status;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.AbstractTask;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/3.
 */
public class ComponentStatus {
    private String host;

    private int port;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
