package com.cufe.taskProcessor.rpc.server;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.StatusEnum;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/3.
 */
public abstract class AbstractComponentInitServer<C extends ComponentContext, RPC_S, RPC_Q> {
    private static final Logger LOGGER = Logger.getLogger(AbstractComponentInitServer.class.getSimpleName());

    protected C componentContext;

    public <Config> RPC_Q init(RPC_S rpcRes) {
        try {
            LOGGER.log(Level.INFO, "SERVER:组件开始初始化,");
            Config config = rpcResToLocal(rpcRes);
            extraInit(config);
            componentContext.init();
            ComponentRelation self = self(config);
            ComponentRelation leader = leader(config);

            self.setLeader(leader);
            componentContext.getComponentRelation().setLeader(leader);

            componentContext.getComponentRelation().setRelationTypeEnum(self.getRelationTypeEnum());
            componentContext.getComponentRelation().setStatus(StatusEnum.INIT);
            //通知leader自己已经添加到leader下
            componentContext.getComponentClientFactory().create(leader).get().leaderClient.notify(self);
            LOGGER.log(Level.INFO, "SERVER:组件初始化成功");
            return localToRPC_Q(true);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SERVER:组件初始化失败", e);
            return localToRPC_Q(false);
        }
    }

    protected abstract <Config> void extraInit(Config config);

    protected abstract <Config> ComponentRelation self(Config config);

    protected abstract <Config> ComponentRelation leader(Config config);

    protected abstract <Config> Config rpcResToLocal(RPC_S rpcRes);

    protected abstract RPC_Q localToRPC_Q(boolean result);
}
