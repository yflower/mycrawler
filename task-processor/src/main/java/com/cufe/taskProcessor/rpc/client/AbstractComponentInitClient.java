package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;

/**
 * Created by jianganlan on 2017/4/13.
 */
public abstract class AbstractComponentInitClient<R extends ComponentRelation> {
     protected R componentRelation;

    public boolean init(int thread) {
        return rpcResToLocalRes(rpcSend(localReqToRpcReq(thread)));

    }

    abstract <RPC_Q> RPC_Q localReqToRpcReq(int thread);

    abstract <RPC_S> boolean rpcResToLocalRes(RPC_S rpcRes);

    abstract <RPC_Q, RPC_S> RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(R componentRelation) {
        this.componentRelation = componentRelation;
    }
}
