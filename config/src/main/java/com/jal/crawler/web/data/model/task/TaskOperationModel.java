package com.jal.crawler.web.data.model.task;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.TaskOperationEnum;

/**
 * Created by jal on 2017/2/25.
 */
public class TaskOperationModel {
    private boolean test;

    private ComponentEnum componentType;

    private TaskOperationEnum taskType;

    private String taskTag;

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public TaskOperationEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskOperationEnum taskType) {
        this.taskType = taskType;
    }

    public ComponentEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentEnum componentType) {
        this.componentType = componentType;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
