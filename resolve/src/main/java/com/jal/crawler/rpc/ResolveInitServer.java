package com.jal.crawler.rpc;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.rpc.server.AbstractComponentInitServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.persist.MongoPersist;
import com.jal.crawler.persist.Persist;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.proto.resolve.RpcResolveConfigGrpc;
import com.mongodb.MongoCredential;
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
public class ResolveInitServer extends RpcResolveConfigGrpc.RpcResolveConfigImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveInitServer.class);
    @Autowired
    private ResolveContext resolveContext;

    private ResolveConfig config;

    @Override
    public void resolveConfig(ResolveConfig request, StreamObserver<ConfigStatus> responseObserver) {
        ComponentInitService initService = new ComponentInitService(resolveContext);
        ConfigStatus configStatus = initService.init(request);
        responseObserver.onNext(configStatus);
        responseObserver.onCompleted();
    }

    @Override
    public void resolveConfigShow(ConfigComponentStatus request, StreamObserver<ResolveConfig> responseObserver) {
        responseObserver.onNext(config);
        responseObserver.onCompleted();
    }

    private boolean isRun(ResolveContext context) {
        return context.getStatus() == StatusEnum.STARTED;
    }

    private class ComponentInitService extends AbstractComponentInitServer<ResolveContext, ResolveConfig, ConfigStatus> {


        public ComponentInitService(ResolveContext resolveContext) {
            this.componentContext = resolveContext;
        }

        @Override
        protected <Config> void extraInit(Config config) {
            ComponentInitService.Config config1 = (ComponentInitService.Config) config;
            ResolveContext context = componentContext;
            context.redisTemplate(config1.redisTemplate);
            context.persist(config1.persist);
//            context.persist(new ConsolePersist());
        }

        @Override
        protected <Config> ComponentRelation self(Config config) {
            ComponentInitService.Config config1 = (ComponentInitService.Config) config;
            ComponentRelation self = new ComponentRelation();
            self.setHost(config1.selfHost);
            self.setPort(config1.selfPort);
            self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(config1.relationType));
            return self;
        }

        @Override
        protected <Config> ComponentRelation leader(Config config) {
            ComponentInitService.Config config1 = (ComponentInitService.Config) config;
            ComponentRelation leader = new ComponentRelation();
            leader.setHost(config1.leaderHost);
            leader.setPort(config1.leaderPort);
            leader.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(config1.relationType));
            return leader;
        }

        @Override
        protected <Config> Config rpcResToLocal(ResolveConfig rpcRes) {
            ComponentInitService.Config config = new ComponentInitService.Config();
            config.thread = rpcRes.getThread();
            //redis设置
            RedisConfig redisConfig = rpcRes.getRedisConfig();
            JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
            shardInfo.setPassword(redisConfig.getPassword());
            RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
            RedisTemplate redisTemplate = new StringRedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.afterPropertiesSet();
            config.redisTemplate = redisTemplate;

            //默认mongo 保存结果
            ResolveConfig.Persist persist = rpcRes.getPersist();
            if (persist == ResolveConfig.Persist.MONGO) {
                config.persist =
                        new MongoPersist(MongoCredential.createScramSha1Credential(
                                rpcRes.getMongoConfig().getUser(),
                                rpcRes.getMongoConfig().getDatabase(),
                                rpcRes.getMongoConfig().getPassword().toCharArray()
                        ),
                                rpcRes.getMongoConfig().getHost(),
                                rpcRes.getMongoConfig().getPort());
            }
            return (Config) config;
        }


        @Override
        protected ConfigStatus localToRPC_Q(boolean result) {
            return ConfigStatus.newBuilder().setInit(result).build();
        }

        private class Config {
            RedisTemplate redisTemplate;
            Persist persist;
            int thread;

            String selfHost;
            String leaderHost;
            int selfPort;
            int leaderPort;


            int relationType;
        }
    }
}
