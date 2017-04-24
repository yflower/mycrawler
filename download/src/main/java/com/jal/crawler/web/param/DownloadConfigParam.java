package com.jal.crawler.web.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadConfigParam {

    @NotNull
    private String host;

    @Min(1)
    private int port;

    @Min(1)
    private int thread;

    @Min(0)
    @Max(1)
    private int relationType;

    @Min(0)
    private int sleepTime;

    private boolean proxy;

    private List<String> proxyAddress;

    private MongoConfigParam mongoConfig;

    private RedisConfigParam redisConfig;

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public List<String> getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(List<String> proxyAddress) {
        this.proxyAddress = proxyAddress;
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
}
