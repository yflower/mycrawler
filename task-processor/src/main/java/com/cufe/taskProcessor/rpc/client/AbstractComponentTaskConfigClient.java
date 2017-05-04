package com.cufe.taskProcessor.rpc.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/5/4.
 */
public abstract class AbstractComponentTaskConfigClient<RPC_S, RPC_Q> {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentTaskConfigClient.class.getSimpleName());

    protected ComponentRelation componentRelation;

    public Map<String, Object> taskConfig(String taskTag) {

        return rpcResToLocalRes(rpcSend(localReqToRpcReq(taskTag)));

    }


    protected abstract RPC_Q localReqToRpcReq(String taskTag);

    protected abstract Map<String, Object> rpcResToLocalRes(RPC_S rpcRes);

    protected abstract RPC_S rpcSend(RPC_Q rpcReq);

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }
}
