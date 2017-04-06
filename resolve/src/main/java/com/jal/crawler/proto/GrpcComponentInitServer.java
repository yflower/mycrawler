package com.jal.crawler.proto;

import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.service.AbstractComponentInitService;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.persist.ConsolePersist;
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
public class GrpcComponentInitServer extends RpcResolveConfigGrpc.RpcResolveConfigImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcComponentInitServer.class);
    @Autowired
    private ResolveContext resolveContext;

    private ResolveConfig config;

    @Override
    public void resolveConfig(ResolveConfig request, StreamObserver<ConfigStatus> responseObserver) {
        ComponentInitService initService = new ComponentInitService();
        initService.setComponentContext(resolveContext);

        int thread = request.getThread();
        //redis设置
        RedisConfig redisConfig = request.getRedisConfig();
        JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
        shardInfo.setPassword(redisConfig.getPassword());
        RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
        RedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        initService.redisTemplate = redisTemplate;

        //默认mongo 保存结果
        ResolveConfig.Persist persist = request.getPersist();
        if (persist == ResolveConfig.Persist.MONGO) {
            initService.persist =
                    new MongoPersist(MongoCredential.createScramSha1Credential(
                            request.getMongoConfig().getUser(),
                            request.getMongoConfig().getDatabase(),
                            request.getMongoConfig().getPassword().toCharArray()
                    ),
                            request.getMongoConfig().getHost(),
                            request.getMongoConfig().getPort());
        }

        boolean init = initService.init(thread);

        this.config = request;
        ConfigStatus status = ConfigStatus.newBuilder().setInit(init).build();
        responseObserver.onNext(status);
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

    private class ComponentInitService extends AbstractComponentInitService {
        RedisTemplate redisTemplate;
        Persist persist;

        @Override
        public void extraInit() {
            ResolveContext context = (ResolveContext) componentContext;
            context.redisTemplate(redisTemplate);
//            context.persist(persist);
            context.persist(new ConsolePersist());
        }
    }
}
