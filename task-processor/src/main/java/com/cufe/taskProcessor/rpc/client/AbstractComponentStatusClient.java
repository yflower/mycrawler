package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.ComponentStatus;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentStatusClient<C extends ComponentContext, R extends ComponentRelation> {
    protected C componentContext;

     R componentRelation;

    public ComponentStatus status() {
        ComponentStatus componentStatus = componentContext.componentStatus(componentType());
        ComponentStatus toStatus = rpcResToLocalRes(rpcSend(localReqToRpcReq(componentStatus)));
        return toStatus;
    }


    abstract int componentType();

    abstract <RPC_Q> RPC_Q localReqToRpcReq(ComponentStatus componentStatus);

    abstract <RPC_S> ComponentStatus rpcResToLocalRes(RPC_S rpcRes);

    abstract <RPC_Q, RPC_S> RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(R componentRelation) {
        this.componentRelation = componentRelation;
    }

}