package com.cufe.taskProcessor;

import com.cufe.taskProcessor.task.AbstractTask;

/**
 * Created by jianganlan on 2017/4/3.
 */
@FunctionalInterface
public interface Repository<R> {
    void persist(AbstractTask task,R object);
}
