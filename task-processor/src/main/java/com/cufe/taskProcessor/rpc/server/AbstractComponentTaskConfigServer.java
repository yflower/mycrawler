package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/5/4.
 */
public abstract class AbstractComponentTaskConfigServer<C extends ComponentContext, RPC_S, RPC_Q> {
    private final static Logger LOGGER = Logger.getLogger(AbstractComponentTaskConfigServer.class.getSimpleName());

    protected ComponentContext componentContext;


    public RPC_Q taskConfig(RPC_S rpcRes) {

        LOGGER.log(Level.INFO, "SERVER:获取任务设置成功");
        return localToRPC_Q(internalTaskConfig(rpcResToLocal(rpcRes)));

    }

    protected abstract <T extends AbstractTask> T internalTaskConfig(String taskTag);

    protected abstract String rpcResToLocal(RPC_S rpcRes);

    protected abstract <T extends AbstractTask> RPC_Q localToRPC_Q(T config);
}
