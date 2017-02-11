package com.jal.crawler.proto;

import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.persist.MongoPersist;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.proto.resolve.RpcResolveConfigGrpc;
import com.mongodb.MongoCredential;
import io.grpc.stub.StreamObserver;
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
public class GrpcConfigServer extends RpcResolveConfigGrpc.RpcResolveConfigImplBase {
    @Autowired
    private ResolveContext resolveContext;

    private ResolveConfig config;

    @Override
    public void resolveConfig(ResolveConfig request, StreamObserver<ConfigStatus> responseObserver) {
        if (isRun(resolveContext)) {

        } else {
            int thread = request.getThread();
            //redis设置
            RedisConfig redisConfig = request.getRedisConfig();
            JedisShardInfo shardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
            shardInfo.setPassword(redisConfig.getPassword());
            RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(shardInfo);
            RedisTemplate redisTemplate = new StringRedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.afterPropertiesSet();
            resolveContext.thread(thread);
            resolveContext.redisTemplate(redisTemplate);
            //默认mongo 保存结果
            ResolveConfig.Persist persist = request.getPersist();
            if (persist == ResolveConfig.Persist.MONGO) {
                resolveContext.persist(
                        new MongoPersist(MongoCredential.createScramSha1Credential(
                                request.getMongoConfig().getUser(),
                                request.getMongoConfig().getDatabase(),
                                request.getMongoConfig().getPassword().toCharArray()
                        ),
                                request.getMongoConfig().getHost(),
                                request.getMongoConfig().getPort()));
            }
            System.out.println("开始启动" + request.toString());
            resolveContext.run();
            this.config = request;
        }
        ConfigStatus status = ConfigStatus.newBuilder().setInit(true).build();
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }

    @Override
    public void resolveConfigShow(ConfigComponentStatus request, StreamObserver<ResolveConfig> responseObserver) {
        responseObserver.onNext(config);
        responseObserver.onCompleted();
    }

    private boolean isRun(ResolveContext context) {
        return context.status() == 2;
    }
}
