package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.TaskTypeEnum;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentTaskClient<RPC_S, RPC_Q> {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentTaskClient.class.getSimpleName());

    protected ComponentRelation componentRelation;

    public <P> boolean task(String taskTag, TaskTypeEnum taskType, P params) {
        boolean result = false;
        if (taskType == TaskTypeEnum.ADD) {
            AbstractTask task = generateTask(taskTag, params);
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


    protected abstract <P> AbstractTask generateTask(String taskTag, P params);

    protected abstract RPC_Q localReqToRpcReq(AbstractTask task, String taskTag, TaskTypeEnum taskType);

    protected abstract boolean rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }
}
