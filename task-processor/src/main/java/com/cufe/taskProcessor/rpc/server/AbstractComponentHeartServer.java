package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.StatusEnum;

import java.util.logging.Logger;

/**
 * Created by jal on 2017/4/24.
 */
public abstract class AbstractComponentHeartServer<C extends ComponentContext, RPC_S, RPC_Q>  {
    private final static Logger LOGGER=Logger.getLogger(AbstractComponentHeartServer.class.getSimpleName());

    protected C componentContext;

    public RPC_Q heart(RPC_S rpcRes){
        StatusEnum fromStatus = rpcResToLocal(rpcRes);
        StatusEnum toStatus = componentContext.getStatus();
        LOGGER.info("heart from:"+fromStatus+"--> to "+toStatus);
        return localToRPC_Q(toStatus);
    }


    protected abstract StatusEnum rpcResToLocal(RPC_S rpcRes);

    protected abstract RPC_Q localToRPC_Q(StatusEnum result);
}
