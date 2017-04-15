package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentStatusClient<C extends ComponentContext, RPC_S, RPC_Q> {
    protected C componentContext;

    ComponentRelation componentRelation;

    public ComponentStatus status() {
        ComponentStatus componentStatus = componentContext.componentStatus(componentType());
        ComponentStatus toStatus = rpcResToLocalRes(rpcSend(localReqToRpcReq(componentStatus)));
        return toStatus;
    }


    protected abstract int componentType();

    protected abstract RPC_Q localReqToRpcReq(ComponentStatus componentStatus);

    protected abstract ComponentStatus rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }

}