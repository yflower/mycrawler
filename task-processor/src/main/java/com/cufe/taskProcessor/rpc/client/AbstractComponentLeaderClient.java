package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

/**
 * Created by jianganlan on 2017/4/23.
 */
public abstract class AbstractComponentLeaderClient< RPC_S, RPC_Q> {
    protected ComponentContext componentContext;

    protected ComponentRelation componentRelation;

    public ComponentStatus notify(ComponentRelation componentRelation) {
        return rpcResToLocalRes(rpcSend(localReqToRpcReq(componentRelation)));
    }

    protected abstract RPC_Q localReqToRpcReq(ComponentRelation componentRelation);

    protected abstract ComponentStatus rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }
}
