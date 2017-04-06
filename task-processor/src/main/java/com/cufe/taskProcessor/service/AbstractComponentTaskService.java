package com.cufe.taskProcessor.service;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.enums.TaskTypeEnum;
import com.cufe.taskProcessor.task.AbstractTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentTaskService {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentTaskService.class.getSimpleName());

    protected ComponentContext componentContext;

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    public boolean task(String taskTag, TaskTypeEnum taskType) {
        boolean result = false;
        if (componentContext.getStatus() == StatusEnum.STARTED || componentContext.getStatus() == StatusEnum.INIT) {
            if (taskType == TaskTypeEnum.ADD) {
                AbstractTask task = generateTask(taskTag);
                task.init();
                task.setStatus(StatusEnum.INIT);
                result = componentContext.addTask(task);
                LOGGER.log(Level.INFO, "添加一个任务 " + task.getTaskTag());
            } else if (taskType == TaskTypeEnum.STOP) {
                result = componentContext.stopTask(taskTag);
                LOGGER.log(Level.INFO, "停止一个任务 " + taskTag);
            } else if (taskType == TaskTypeEnum.FINISH) {
                result = componentContext.finishTask(taskTag);
                LOGGER.log(Level.INFO, "完成一个任务 " + taskTag);
            } else if (taskType == TaskTypeEnum.DESTROY) {
                result = componentContext.destroyTask(taskTag);
                LOGGER.log(Level.INFO, "销毁一个任务 " + taskTag);
            }

        }
        return result;
    }

    public abstract AbstractTask generateTask(String taskTag);
}
