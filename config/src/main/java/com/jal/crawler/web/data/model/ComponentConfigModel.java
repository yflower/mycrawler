package com.jal.crawler.web.data.model;

import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;

/**
 * Created by jal on 2017/2/25.
 */
public abstract class ComponentConfigModel extends ComponentModel {
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
}
