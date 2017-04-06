package com.cufe.taskProcessor;

import com.cufe.taskProcessor.task.AbstractTask;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/3.
 */
@FunctionalInterface
public interface Processor<S, R> {
    Optional<R> processor(AbstractTask task, S resource);
}
