package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import com.jal.crawler.web.service.IDBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jal on 2017/2/18.
 */
@Service
public class DBConfigServiceImpl implements IDBConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IDBConfigService.class);

    @Resource
    private ConfigContext configContext;


    @Override
    public MongoConfigModel mongo() {
        return configContext.getMongoConfigModel();
    }

    @Override
    public RedisConfigModel redis() {
        return configContext.getRedisConfigModel();
    }

    @Override
    public void mongoConfig(MongoConfigModel mongoConfigModel) {
        try {
            configContext.setMongoConfigModel(mongoConfigModel);
        } catch (Exception ex) {
            throw new BizException(ExceptionEnum.DB_CONFIG_ERROR);
        }
    }

    @Override
    public void redisConfig(RedisConfigModel redisConfigModel) {
        try {
            configContext.setRedisConfigModel(redisConfigModel);

        } catch (Exception e) {
            throw new BizException(ExceptionEnum.DB_CONFIG_ERROR);

        }
    }


}
