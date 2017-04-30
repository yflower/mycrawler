package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.task.StatusEnum;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentInitClient<RPC_S, RPC_Q> {
    protected ComponentRelation componentRelation;

    public <P> boolean init(P params) {
        boolean result = rpcResToLocalRes(rpcSend(localReqToRpcReq(params)));
        if (result) {
            componentRelation.setStatus(StatusEnum.INIT);
        }
        return result;

    }

    protected abstract <P> RPC_Q localReqToRpcReq(P params);

    protected abstract boolean rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }
}
