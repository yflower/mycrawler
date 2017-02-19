package com.jal.crawler.web.data.model;

/**
 * Created by jal on 2017/2/19.
 */
public class ResolveConfigModel {
    private String host;

    private int port;

    private int thread;

    private MongoConfigModel mongoConfigModel;

    private RedisConfigModel redisConfigModel;


    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public MongoConfigModel getMongoConfigModel() {
        return mongoConfigModel;
    }

    public void setMongoConfigModel(MongoConfigModel mongoConfigModel) {
        this.mongoConfigModel = mongoConfigModel;
    }

    public RedisConfigModel getRedisConfigModel() {
        return redisConfigModel;
    }

    public void setRedisConfigModel(RedisConfigModel redisConfigModel) {
        this.redisConfigModel = redisConfigModel;
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
}
