package com.jal.crawler.web.service;

import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.param.MongoConfigParam;
import com.jal.crawler.web.data.param.RedisConfigParam;
import com.jal.crawler.web.data.view.DBConfigVO;

/**
 * Created by jal on 2017/2/18.
 */
public interface IDBConfigService {
    DBConfigVO showDBConfig();

    void mongoConfig(MongoConfigParam mongoConfigParam) throws DBConfigException;

    void redisConfig(RedisConfigParam redisConfigParam) throws DBConfigException;
}
