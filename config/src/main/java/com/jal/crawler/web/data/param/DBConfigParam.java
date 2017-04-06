package com.jal.crawler.web.data.param;

/**
 * Created by jianganlan on 2017/3/26.
 */
public class DBConfigParam {
    private RedisConfigParam redis;

    private MongoConfigParam mongo;

    public RedisConfigParam getRedis() {
        return redis;
    }

    public void setRedis(RedisConfigParam redis) {
        this.redis = redis;
    }

    public MongoConfigParam getMongo() {
        return mongo;
    }

    public void setMongo(MongoConfigParam mongo) {
        this.mongo = mongo;
    }
}
