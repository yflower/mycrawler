package com.cufe.taskProcessor;

import com.cufe.taskProcessor.task.AbstractTask;

/**
 * Created by jal on 2017/4/9.
 */
public interface ProcessorHook<T extends AbstractTask> {
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
