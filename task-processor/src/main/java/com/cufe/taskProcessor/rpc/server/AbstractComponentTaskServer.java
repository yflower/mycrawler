package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.TaskTypeEnum;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentTaskServer<C extends ComponentContext> {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentTaskServer.class.getSimpleName());

    protected C componentContext;

    public <RPC_S, RPC_Q> RPC_Q task(RPC_S rpc_s) {
        boolean result = false;
        Map<String, Object> taskOp = rpcResToLocal(rpc_s);
        TaskTypeEnum taskType = (TaskTypeEnum) taskOp.get("taskType");
        String taskTag = String.valueOf(taskOp.get("taskTag"));

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
        return localToRPC_Q(result);
    }

    abstract AbstractTask generateTask(String taskTag);

    abstract <RPC_S, Local> Local rpcResToLocal(RPC_S rpcRes);

    abstract <Result, RPC_Q> RPC_Q localToRPC_Q(Result result);
}
