package com.jal.crawler.web.param;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveConfigParam {
    @NotNull
    private String host;

    @Min(1)
    private int port;

    @Min(1)
    private int httpPort;

    @Min(1)
    private int thread;

    @Min(0)@Max(1)
    private int relationType;

    private MongoConfigParam mongoConfig;

    private RedisConfigParam redisConfig;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public MongoConfigParam getMongoConfig() {
        return mongoConfig;
    }

    public void setMongoConfig(MongoConfigParam mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    public RedisConfigParam getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfigParam redisConfig) {
        this.redisConfig = redisConfig;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }
}
