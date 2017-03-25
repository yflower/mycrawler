package com.jal.crawler.web.data.model.taskOperation;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.TaskOperationEnum;

/**
 * Created by jal on 2017/2/25.
 */
public class TaskOperationModel {
    private ComponentEnum componentType;

    private TaskOperationEnum type;

    private String taskTag;

    public String getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public TaskOperationEnum getType() {
        return type;
    }

    public void setType(TaskOperationEnum type) {
        this.type = type;
    }

    public ComponentEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentEnum componentType) {
        this.componentType = componentType;
    }
}
