package com.jal.crawler.web.param.rpc;

import com.cufe.taskProcessor.ComponentFacade;
import com.jal.crawler.web.param.MongoConfigParam;
import com.jal.crawler.web.param.RedisConfigParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class LinkConfigRpcParam extends ComponentFacade.initParam {
    private MongoConfigParam mongoConfig;

    private RedisConfigParam redisConfig;


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
