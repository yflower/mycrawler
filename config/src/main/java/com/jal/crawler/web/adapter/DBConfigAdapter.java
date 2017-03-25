package com.jal.crawler.web.adapter;

import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import com.jal.crawler.web.data.param.MongoConfigParam;
import com.jal.crawler.web.data.param.RedisConfigParam;
import com.jal.crawler.web.data.view.DBConfigVO;

/**
 * Created by jal on 2017/2/19.
 */
public abstract class DBConfigAdapter {
    protected DBConfigVO modelToView(MongoConfigModel mongoConfigModel, RedisConfigModel redisConfigModel) {
        DBConfigVO dbConfigVO = new DBConfigVO();
        if (mongoConfigModel != null) {
            dbConfigVO.setMongoConfig(mongoConfigModel.getUser() + ":" + mongoConfigModel.getPassword() + "@" +
                    mongoConfigModel.getHost() + ":" + mongoConfigModel.getPort() + "/" + mongoConfigModel.getDatabase());
        }
        if (redisConfigModel != null) {
            dbConfigVO.setRedisConfig(redisConfigModel.getPassword() + "@" + redisConfigModel.getHost() + ":" + redisConfigModel.getPort());
        }
        return dbConfigVO;
    }

    protected RedisConfigModel viewToModel(RedisConfigParam redisConfigParam) {
        RedisConfigModel redisConfigModel = new RedisConfigModel();
        redisConfigModel.setHost(redisConfigParam.getHost());
        redisConfigModel.setPort(redisConfigParam.getPort());
        redisConfigModel.setPassword(redisConfigParam.getPassword());
        return redisConfigModel;
    }

    protected MongoConfigModel viewToModel(MongoConfigParam mongoConfigParam) {
        MongoConfigModel mongoConfigModel = new MongoConfigModel();
        mongoConfigModel.setHost(mongoConfigParam.getHost());
        mongoConfigModel.setPort(mongoConfigParam.getPort());
        mongoConfigModel.setUser(mongoConfigParam.getUser());
        mongoConfigModel.setPassword(mongoConfigParam.getPassword());
        mongoConfigModel.setDatabase(mongoConfigParam.getDatabase());
        return mongoConfigModel;
    }
}
