package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.TaskTypeEnum;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentTaskClient< R extends ComponentRelation> {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentTaskClient.class.getSimpleName());

     R componentRelation;

    public <T extends AbstractTask> boolean task(String taskTag, TaskTypeEnum taskType) {
        boolean result = false;
        if (taskType == TaskTypeEnum.ADD) {
            T task = generateTask(taskTag);
            result = rpcResToLocalRes(rpcSend(localReqToRpcReq(task, taskTag, taskType)));
            LOGGER.log(Level.INFO, "发送一个添加任务 " + task.getTaskTag());
        } else if (taskType == TaskTypeEnum.STOP) {
            result = rpcResToLocalRes(rpcSend(localReqToRpcReq(null, taskTag, taskType)));
            LOGGER.log(Level.INFO, "停止一个任务 " + taskTag);
        } else if (taskType == TaskTypeEnum.FINISH) {
            result = rpcResToLocalRes(rpcSend(localReqToRpcReq(null, taskTag, taskType)));
            LOGGER.log(Level.INFO, "完成一个任务 " + taskTag);
        } else if (taskType == TaskTypeEnum.DESTROY) {
            result = rpcResToLocalRes(rpcSend(localReqToRpcReq(null, taskTag, taskType)));
            LOGGER.log(Level.INFO, "销毁一个任务 " + taskTag);
        }

        return result;
    }


    abstract <T extends AbstractTask> T generateTask(String taskTag);

    abstract <RPC_Q, T extends AbstractTask> RPC_Q localReqToRpcReq(T task, String taskTag, TaskTypeEnum taskType);

    abstract <RPC_S> boolean rpcResToLocalRes(RPC_S rpcRes);

    abstract <RPC_Q, RPC_S> RPC_S rpcSend(RPC_Q rpcReq);


    public void setComponentRelation(R componentRelation) {
        this.componentRelation = componentRelation;
    }

}
