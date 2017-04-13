package com.cufe.taskProcessor;

import com.cufe.taskProcessor.task.AbstractTask;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/3.
 */
@FunctionalInterface
public interface Processor<S, R, T extends AbstractTask> {
    Optional<R> processor(AbstractTask task, S resource);


    default void taskBeforeHook(T task) {
    }


    default void taskAfterHook(T task) {
    }


    default void cycleBeforeHook() {
    }


    default void cycleErrorHook() {
    }


    default void cycleFinalHook() {
    }

}
