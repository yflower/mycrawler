package com.cufe.taskProcessor.service;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.model.ComponentStatus;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentStatusService<C extends ComponentContext> {

    protected C componentContext;


    public ComponentStatus componentStatus(ComponentStatus componentStatus) {
        ComponentStatus self = new ComponentStatus();
        self.setComponentType(componentType());
        self.setComponentStatus(componentContext.getStatus());
        if (componentContext.getStatus() == StatusEnum.STARTED) {
            self.setTasks(componentContext.getTasks());
        }
        return self;

    }


    public abstract int componentType();
}
