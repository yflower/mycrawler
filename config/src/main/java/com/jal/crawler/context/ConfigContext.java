package com.jal.crawler.context;

import com.jal.crawler.web.data.model.MongoConfigModel;
import com.jal.crawler.web.data.model.RedisConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 整个config的context
 * Created by jal on 2017/2/19.
 */
@Component
public class ConfigContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigContext.class);

    private MongoConfigModel mongoConfigModel;

    private RedisConfigModel redisConfigModel;


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
