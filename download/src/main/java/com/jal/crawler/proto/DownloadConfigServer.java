package com.jal.crawler.proto;

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

/**
 * Created by home on 2017/1/24.
 */
@Component
public class DownloadConfigServer extends RpcDownlandConfigGrpc.RpcDownlandConfigImplBase {

    private static final Logger logger = LoggerFactory.getLogger(DownloadConfigServer.class);

    @Autowired
    private DownLoadContext downLoadContext;

    private DownloadConfig config;

    @Override
    public void downloadConfig(DownloadConfig request, StreamObserver<ConfigStatus> responseObserver) {
        int thread = request.getThread();
        int sleep = request.getSleepTime();
        boolean proxy = request.getProxy();
        downLoadContext
                .thread(thread)
                .sleep(sleep);
        if (proxy) {
            //目前只能设定单代理
            downLoadContext.proxy(request.getProxyAddressList().get(0));
        }
        //只接受redis的中间存储
        if (request.getPersist() == DownloadConfig.Persist.REDIS) {
            RedisConfig redisConfig = request.getRedisConfig();
            JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
            shardInfo.setPassword(redisConfig.getPassword());
            RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
            RedisTemplate redisTemplate = new StringRedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.afterPropertiesSet();
            downLoadContext.redisTemplate(redisTemplate);
        }
        ConfigStatus.Builder builder = ConfigStatus.newBuilder();
        //启动组件
        try {
            downLoadContext.run();
            builder.setInit(true);
            logger.info("start download component \n {}", request);
        } catch (Exception ex) {
            builder.setInit(false);
            logger.info("fail to start download component \n {}", request);
        }
        this.config = request;
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void downloadConfigShow(ConfigComponentStatus request, StreamObserver<DownloadConfig> responseObserver) {
        responseObserver.onNext(config);
        responseObserver.onCompleted();
    }
}
