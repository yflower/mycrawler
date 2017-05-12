package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.StatusEnum;

/**
 * Created by jal on 2017/4/24.
 */
public abstract class AbstractComponentHeartClient< RPC_S, RPC_Q> {
    protected ComponentRelation componentRelation;

    protected ComponentContext componentContext;


    public StatusEnum heart(){
        return rpcResToLocalRes(rpcSend(localReqToRpcReq(componentContext.getStatus())));
    }

    protected abstract RPC_Q localReqToRpcReq(StatusEnum fromStatus);

    protected abstract StatusEnum rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }
}
