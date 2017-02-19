package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.adapter.DBConfigAdapter;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.model.MongoConfigModel;
import com.jal.crawler.web.data.model.RedisConfigModel;
import com.jal.crawler.web.data.param.MongoConfigParam;
import com.jal.crawler.web.data.param.RedisConfigParam;
import com.jal.crawler.web.data.view.DBConfigVO;
import com.jal.crawler.web.service.IDBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jal on 2017/2/18.
 */
@Service
public class DBConfigServiceImpl extends DBConfigAdapter implements IDBConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IDBConfigService.class);

    @Resource
    private ConfigContext configContext;

    @Override
    public DBConfigVO showDBConfig() {
        return modelToView(configContext.getMongoConfigModel(), configContext.getRedisConfigModel());
    }

    @Override
    public void mongoConfig(MongoConfigParam mongoConfigParam) throws DBConfigException {
        MongoConfigModel mongoConfigModel = viewToModel(mongoConfigParam);
        try {
            configContext.setMongoConfigModel(mongoConfigModel);
        } catch (Exception ex) {
            throw new DBConfigException("mongo config exception ,concat admin");
        }


    }

    @Override
    public void redisConfig(RedisConfigParam redisConfigParam) throws DBConfigException {
        RedisConfigModel redisConfigModel = viewToModel(redisConfigParam);
        try {
            configContext.setRedisConfigModel(redisConfigModel);

        } catch (Exception e) {
            throw new DBConfigException("redis config exception ,concat admin");

        }

    }

}
