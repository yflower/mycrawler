package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.component.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentStatusServer<C extends ComponentContext> {

    private final static Logger LOGGER = Logger.getLogger(AbstractComponentStatusServer.class.getSimpleName());

    protected C componentContext;


    public <RPC_S, RPC_Q> RPC_Q componentStatus(RPC_S rpcRes) {
        int componentType = componentType();

        ComponentStatus componentStatus = rpcResToLocal(rpcRes);

        ComponentStatus result = componentContext.componentStatus(componentType);

        LOGGER.log(Level.INFO, "获取组件状态");

        return localToRPC_Q(result);

    }


    abstract int componentType();

    abstract <RPC_S, Local> Local rpcResToLocal(RPC_S rpcRes);

    abstract <Result, RPC_Q> RPC_Q localToRPC_Q(Result result);
}
