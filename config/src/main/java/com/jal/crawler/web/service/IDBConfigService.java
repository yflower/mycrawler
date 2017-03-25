package com.jal.crawler.web.service;

import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;

/**
 * Created by jal on 2017/2/18.
 */
public interface IDBConfigService {
    MongoConfigModel mongo();

    RedisConfigModel redis();

    void mongoConfig(MongoConfigModel mongoConfigModel) throws DBConfigException;

    void redisConfig(RedisConfigModel redisConfigModel) throws DBConfigException;
}
