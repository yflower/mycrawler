package com.jal.crawler.rpc;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.rpc.server.AbstractComponentInitServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DataContext;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.data.DataConfig;
import com.jal.crawler.proto.data.RpcDataConfigGrpc;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.resource.DataFetch;
import com.jal.crawler.resource.MongoDataFetch;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jal on 2017/1/23.
 */
@Component
public class DataInitServer extends RpcDataConfigGrpc.RpcDataConfigImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitServer.class);
    @Autowired
    private DataContext dataContext;

    private DataConfig config;

    @Override
    public void dataConfig(DataConfig request, StreamObserver<ConfigStatus> responseObserver) {
        DataInitService initService = new DataInitService(dataContext);
        ConfigStatus configStatus = initService.init(request);
        responseObserver.onNext(configStatus);
        responseObserver.onCompleted();
    }


    @Override
    public void dataConfigShow(ComponentStatus request, StreamObserver<DataConfig> responseObserver) {
        responseObserver.onNext(config);
        responseObserver.onCompleted();
    }


    private class DataInitService extends AbstractComponentInitServer<DataContext, DataConfig, ConfigStatus> {


        public DataInitService(DataContext dataContext) {
            this.componentContext = dataContext;
        }

        @Override
        protected <Config> void extraInit(Config config) {
            DataInitService.Config config1 = (DataInitService.Config) config;
            DataContext context = componentContext;
            componentContext.setThread(config1.thread);
            context.setDataFetch(config1.dataFetch);
        }

        @Override
        protected <Config> ComponentRelation self(Config config) {
            DataInitService.Config config1 = (DataInitService.Config) config;
            ComponentRelation self = new ComponentRelation();
            self.setHost(config1.selfHost);
            self.setPort(config1.selfPort);
            self.setServerPort(config1.selfHttpPort);
            self.setStatus(StatusEnum.numberOf(config1.selfStatus));
            self.setComponentType(dataContext.componentType());
            self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(config1.relationType));
            return self;
        }

        @Override
        protected <Config> ComponentRelation leader(Config config) {
            DataInitService.Config config1 = (DataInitService.Config) config;
            ComponentRelation leader = new ComponentRelation();
            leader.setHost(config1.leaderHost);
            leader.setPort(config1.leaderPort);
            leader.setComponentType(dataContext.componentType());
            leader.setServerPort(config1.leaderHttpPort);
            leader.setRelationTypeEnum(ComponentRelationTypeEnum.LEADER);
            leader.setStatus(StatusEnum.numberOf(config1.leaderStatus));
            return leader;
        }

        @Override
        protected <Config> Config rpcResToLocal(DataConfig rpcRes) {
            DataInitService.Config config = new DataInitService.Config();
            config.thread = rpcRes.getThread();
            //redis设置
            config.thread = rpcRes.getThread();
            config.selfStatus = rpcRes.getSelfStatusValue();
            config.leaderStatus = rpcRes.getLeaderStatusValue();
            config.selfHost = rpcRes.getSelfHost();
            config.selfPort = rpcRes.getSelfPort();
            config.leaderHost = rpcRes.getLeaderHost();
            config.leaderPort = rpcRes.getLeaderPort();
            config.relationType = rpcRes.getRelationType();
            config.selfStatus = rpcRes.getSelfStatusValue();
            config.leaderStatus = rpcRes.getLeaderStatusValue();
            config.selfHttpPort=rpcRes.getSelfHttpPort();
            config.leaderHttpPort=rpcRes.getLeaderHttpPort();
            config.dataFetch = MongoDataFetch.build(rpcRes.getMongoConfig().getHost(),
                    rpcRes.getMongoConfig().getPort(),
                    rpcRes.getMongoConfig().getUser(),
                    rpcRes.getMongoConfig().getPassword(),
                    rpcRes.getMongoConfig().getDatabase());


            return (Config) config;
        }


        @Override
        protected ConfigStatus localToRPC_Q(boolean result) {
            return ConfigStatus.newBuilder().setInit(result).build();
        }

        private class Config {
            DataFetch dataFetch;
            int thread;

            String selfHost;
            String leaderHost;
            int selfPort;
            int leaderPort;
            int selfStatus;
            int leaderStatus;
            int selfHttpPort;
            int leaderHttpPort;
            int relationType;
        }
    }
}
