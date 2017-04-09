package com.cufe.taskProcessor.service;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.enums.StatusEnum;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentInitService<C extends ComponentContext> {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentInitService.class.getSimpleName());

    protected C componentContext;

    public boolean init(int thread) {
        componentContext.setThread(thread);
        try {
            LOGGER.log(Level.INFO, "组件开始初始化");
            extraInit();
            componentContext.init();
            componentContext.setStatus(StatusEnum.INIT);
            LOGGER.log(Level.INFO, "组件初始化成功");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "组件初始化失败", e);
            return false;
        }
    }

    public abstract void extraInit();
}
