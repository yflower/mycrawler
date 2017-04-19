package com.jal.crawler.web.param;

import com.cufe.taskProcessor.ComponentFacade;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadConfigParam extends ComponentFacade.initParam {

    private int sleepTime;

    private boolean proxy;

    private List<String> proxyAddress;


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
