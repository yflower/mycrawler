package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentStatusServer<RPC_S, RPC_Q> {

    private final static Logger LOGGER = Logger.getLogger(AbstractComponentStatusServer.class.getSimpleName());

    protected ComponentContext componentContext;


    public RPC_Q componentStatus(RPC_S rpcRes) {

        ComponentStatus result = componentContext.componentStatus();

        LOGGER.log(Level.INFO, "SERVER:获取组件状态成功");

        return localToRPC_Q(result);

    }



    protected abstract ComponentStatus rpcResToLocal(RPC_S rpcRes);

    protected abstract RPC_Q localToRPC_Q(ComponentStatus result);
}
