package com.jal.crawler.rpc;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.rpc.server.AbstractComponentInitServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.LinkContext;
import com.jal.crawler.fetch.LinkFetch;
import com.jal.crawler.fetch.MongoLinkFetch;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.link.LinkConfig;
import com.jal.crawler.proto.link.RpcLinkConfigGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by jal on 2017/1/23.
 */
@Component
public class LinkInitServer extends RpcLinkConfigGrpc.RpcLinkConfigImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkInitServer.class);
    @Autowired
    private LinkContext linkContext;

    private LinkConfig config;

    @Override
    public void linkConfig(LinkConfig request, StreamObserver<ConfigStatus> responseObserver) {
        ComponentInitService initService = new ComponentInitService(linkContext);
        ConfigStatus configStatus = initService.init(request);
        responseObserver.onNext(configStatus);
        responseObserver.onCompleted();
    }


    private class ComponentInitService extends AbstractComponentInitServer<LinkContext, LinkConfig, ConfigStatus> {


        public ComponentInitService(LinkContext linkContext) {
            this.componentContext = linkContext;
        }

        @Override
        protected <Config> void extraInit(Config config) {
            ComponentInitService.Config config1 = (ComponentInitService.Config) config;
            LinkContext context = componentContext;
            componentContext.setThread(config1.thread);
            context.setRedisTemplate(config1.redisTemplate);
            context.setLinkFetch(config1.dataFetch);
        }

        @Override
        protected <Config> ComponentRelation self(Config config) {
            ComponentInitService.Config config1 = (ComponentInitService.Config) config;
            ComponentRelation self = new ComponentRelation();
            self.setHost(config1.selfHost);
            self.setPort(config1.selfPort);
            self.setComponentType(componentContext.componentType());
            self.setStatus(StatusEnum.numberOf(config1.selfStatus));
            self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(config1.relationType));
            return self;
        }

        @Override
        protected <Config> ComponentRelation leader(Config config) {
            ComponentInitService.Config config1 = (ComponentInitService.Config) config;
            ComponentRelation leader = new ComponentRelation();
            leader.setHost(config1.leaderHost);
            leader.setPort(config1.leaderPort);
            leader.setComponentType(componentContext.componentType());
            leader.setRelationTypeEnum(ComponentRelationTypeEnum.LEADER);
            leader.setStatus(StatusEnum.numberOf(config1.leaderStatus));
            return leader;
        }

        @Override
        protected <Config> Config rpcResToLocal(LinkConfig rpcRes) {
            ComponentInitService.Config config = new ComponentInitService.Config();
            config.thread = rpcRes.getThread();
            //redis设置
            RedisConfig redisConfig = rpcRes.getRedisConfig();
            JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
            shardInfo.setPassword(redisConfig.getPassword());
            RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.afterPropertiesSet();
            config.redisTemplate = redisTemplate;
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
            config.dataFetch = MongoLinkFetch.build(rpcRes.getMongoConfig().getHost(),
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
            RedisTemplate redisTemplate;
            LinkFetch dataFetch;
            int thread;

            String selfHost;
            String leaderHost;
            int selfPort;
            int leaderPort;
            int selfStatus;
            int leaderStatus;

            int relationType;
        }
    }
}
