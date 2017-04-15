package com.jal.crawler.web.param;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadConfigParam {
    private int sleepTime;

    private boolean proxy;

    private List<String> proxyAddress;

    private int thread;

    private MongoConfigParam mongoConfigParam;

    private RedisConfigParam redisConfigParam;

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

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public MongoConfigParam getMongoConfigParam() {
        return mongoConfigParam;
    }

    public void setMongoConfigParam(MongoConfigParam mongoConfigParam) {
        this.mongoConfigParam = mongoConfigParam;
    }

    public RedisConfigParam getRedisConfigParam() {
        return redisConfigParam;
    }

    public void setRedisConfigParam(RedisConfigParam redisConfigParam) {
        this.redisConfigParam = redisConfigParam;
    }
}
