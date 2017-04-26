package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationHolder;
import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;

import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/23.
 */
public abstract class AbstractComponentLeaderServer< RPC_S, RPC_Q> {
    private final static Logger LOGGER = Logger.getLogger(AbstractComponentLeaderServer.class.getSimpleName());

    protected ComponentContext componentContext;

    public RPC_Q receive(RPC_S rpcRes) {
        ComponentRelation relation = rpcResToLocal(rpcRes);

        ComponentRelationHolder relationHolder = componentContext.getComponentRelationHolder();

        relationHolder.addRelation(relation);

        LOGGER.info("leader列表添加组件" + relation);

        return localToRPC_Q(componentContext.componentStatus(componentType()));
    }

    protected abstract ComponentRelation rpcResToLocal(RPC_S rpcRes);

    protected abstract int componentType();

    protected abstract RPC_Q localToRPC_Q(ComponentStatus result);
}
