package com.jal.crawler.rpc;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.rpc.server.AbstractComponentInitServer;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
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

import java.util.List;

/**
 * Created by home on 2017/1/24.
 */
@Component
public class DownloadInitServer extends RpcDownlandConfigGrpc.RpcDownlandConfigImplBase {

    private static final Logger logger = LoggerFactory.getLogger(DownloadInitServer.class);

    @Autowired
    private DownLoadContext downLoadContext;

    private DownloadConfig config;

    @Override
    public void downloadConfig(DownloadConfig request, StreamObserver<ConfigStatus> responseObserver) {
        ComponentInitService initService = new ComponentInitService(downLoadContext);

        ConfigStatus configStatus = initService.init(request);
        responseObserver.onNext(configStatus);
        responseObserver.onCompleted();

    }

    @Override
    public void downloadConfigShow(ConfigComponentStatus request, StreamObserver<DownloadConfig> responseObserver) {
        responseObserver.onNext(config);
        responseObserver.onCompleted();
    }

    private class ComponentInitService extends AbstractComponentInitServer<DownLoadContext, DownloadConfig, ConfigStatus> {
        public ComponentInitService(DownLoadContext context) {
            super.componentContext = context;
        }


        @Override
        protected <Config> void extraInit(Config config) {
            config config1 = (ComponentInitService.config) config;
            DownLoadContext downLoadContext = componentContext;
            downLoadContext.sleep(config1.sleep);
            if (config1.proxy) {
                //目前只能设定单代理
                downLoadContext.proxy(config1.proxyAddress.get(0));
            }
            downLoadContext.redisTemplate(config1.redisTemplate);

            ComponentRelation self = new ComponentRelation();
            self.setHost(config1.selfHost);
            self.setInitPort(config1.selfInitPort);
            self.setStatusPort(config1.selfStatusPort);
            self.setTaskPort(config1.selfTaskPort);
            self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(config1.relationType));

            ComponentRelation leader = new ComponentRelation();
            leader.setHost(config1.leaderHost);
            leader.setInitPort(config1.leaderInitPort);
            leader.setStatusPort(config1.leaderStatusPort);
            leader.setTaskPort(config1.leaderTaskPort);
            leader.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(config1.relationType));

            downLoadContext.componentStart(self, leader);
        }

        @Override
        protected <Config> Config rpcResToLocal(DownloadConfig rpcRes) {
            config config = new config();
            config.sleep = rpcRes.getSleepTime();
            config.proxy = rpcRes.getProxy();
            config.proxyAddress = rpcRes.getProxyAddressList();
            config.selfHost = rpcRes.getSelfHost();
            config.selfInitPort = rpcRes.getSelfInitPort();
            config.selfTaskPort = rpcRes.getSelfTaskPort();
            config.selfStatusPort = rpcRes.getSelfStatusPort();
            config.leaderHost = rpcRes.getLeaderHost();
            config.leaderInitPort = rpcRes.getLeaderInitPort();
            config.leaderStatusPort = rpcRes.getLeaderStatusPort();
            config.leaderTaskPort = rpcRes.getLeaderTaskPort();
            config.relationType = rpcRes.getRelationType();

            if (rpcRes.getPersist() == DownloadConfig.Persist.REDIS) {
                RedisConfig redisConfig = rpcRes.getRedisConfig();
                JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
                shardInfo.setPassword(redisConfig.getPassword());
                RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
                RedisTemplate redisTemplate = new StringRedisTemplate();
                redisTemplate.setConnectionFactory(redisConnectionFactory);
                redisTemplate.afterPropertiesSet();
                config.redisTemplate = redisTemplate;
            }

            return (Config) config;
        }

        @Override
        protected ConfigStatus localToRPC_Q(boolean result) {
            return ConfigStatus.newBuilder().setInit(result).build();
        }

        public class config {
            int sleep;
            boolean proxy;
            List<String> proxyAddress;
            RedisTemplate redisTemplate;

            String selfHost;
            String leaderHost;
            int selfInitPort;
            int selfStatusPort;
            int selfTaskPort;
            int leaderInitPort;
            int leaderStatusPort;
            int leaderTaskPort;

            int relationType;

        }
    }
}
