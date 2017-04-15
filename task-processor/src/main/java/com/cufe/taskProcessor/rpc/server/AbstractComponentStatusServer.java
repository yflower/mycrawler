package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentStatusServer<C extends ComponentContext, RPC_S, RPC_Q> {

    private final static Logger LOGGER = Logger.getLogger(AbstractComponentStatusServer.class.getSimpleName());

    protected C componentContext;


    public RPC_Q componentStatus(RPC_S rpcRes) {
        int componentType = componentType();


        ComponentStatus result = componentContext.componentStatus(componentType);

        LOGGER.log(Level.INFO, "获取组件状态");

        return localToRPC_Q(result);

    }


    protected abstract int componentType();

    protected abstract  ComponentStatus rpcResToLocal(RPC_S rpcRes);

    protected abstract  RPC_Q localToRPC_Q(ComponentStatus result);
}
