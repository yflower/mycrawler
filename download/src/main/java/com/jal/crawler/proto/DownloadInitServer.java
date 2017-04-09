package com.jal.crawler.proto;

import com.cufe.taskProcessor.service.AbstractComponentInitService;
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

        initService.sleep = request.getSleepTime();
        initService.proxy = request.getProxy();
        initService.proxyAddress = request.getProxyAddressList();

        //只接受redis的中间存储
        if (request.getPersist() == DownloadConfig.Persist.REDIS) {
            RedisConfig redisConfig = request.getRedisConfig();
            JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
            shardInfo.setPassword(redisConfig.getPassword());
            RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
            RedisTemplate redisTemplate = new StringRedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.afterPropertiesSet();
            initService.redisTemplate = redisTemplate;
        }
        boolean init = initService.init(request.getThread());


        ConfigStatus.Builder builder = ConfigStatus.newBuilder();
        //启动组件
        builder.setInit(init);
        this.config = request;
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void downloadConfigShow(ConfigComponentStatus request, StreamObserver<DownloadConfig> responseObserver) {
        responseObserver.onNext(config);
        responseObserver.onCompleted();
    }

    private class ComponentInitService extends AbstractComponentInitService<DownLoadContext> {
        public ComponentInitService(DownLoadContext context) {
            super.componentContext = context;
        }

        int sleep;
        boolean proxy;
        List<String> proxyAddress;
        RedisTemplate redisTemplate;

        @Override
        public void extraInit() {
            DownLoadContext downLoadContext = componentContext;
            downLoadContext.sleep(sleep);
            if (proxy) {
                //目前只能设定单代理
                downLoadContext.proxy(proxyAddress.get(0));
            }
            downLoadContext.redisTemplate(redisTemplate);
        }

    }
}
