package com.cufe.taskProcessor.help;

import com.cufe.taskProcessor.task.AbstractTask;

import java.util.function.Consumer;

/**
 * Created by jianganlan on 2017/4/3.
 */
public class FunctionHelp {
    public static Consumer<AbstractTask> TASK_CYCLE_EMPTY_HOOK=task -> {};

    public static Runnable CYCLE_EMPTY_HOOK=()->{};
}
