package com.jal.crawler.web.param.rpc;

import com.cufe.taskProcessor.ComponentFacade;
import com.jal.crawler.web.param.MongoConfigParam;
import com.jal.crawler.web.param.RedisConfigParam;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/23.
 */
public class DownloadRpcConfigParam extends ComponentFacade.initParam {
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
}
