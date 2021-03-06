package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentStatusClient< RPC_S, RPC_Q> {
    protected ComponentContext componentContext;

    protected ComponentRelation componentRelation;

    public ComponentStatus status() {
        ComponentStatus componentStatus = componentContext.componentStatus();
        ComponentStatus toStatus = rpcResToLocalRes(rpcSend(localReqToRpcReq(componentStatus)));
        return toStatus;
    }


    protected abstract RPC_Q localReqToRpcReq(ComponentStatus componentStatus);

    protected abstract ComponentStatus rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }
}