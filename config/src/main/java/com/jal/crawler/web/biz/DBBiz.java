package com.jal.crawler.web.biz;

import com.jal.crawler.web.convert.DBConfigConvert;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import com.jal.crawler.web.data.param.DBConfigParam;
import com.jal.crawler.web.data.param.MongoConfigParam;
import com.jal.crawler.web.data.param.RedisConfigParam;
import com.jal.crawler.web.data.view.DBConfigVO;
import com.jal.crawler.web.service.IDBConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by jal on 2017/2/25.
 */
@Component
public class DBBiz {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBBiz.class);

    @Resource
    private IDBConfigService dbConfigService;

    public void dbConfig(RedisConfigParam redisConfigParam) {
        RedisConfigModel redisConfigModel = DBConfigConvert.paramToModel(redisConfigParam);
        dbConfigService.redisConfig(redisConfigModel);
        LOGGER.info("redis数据库设置成功\n{}", redisConfigModel);
    }

    public void dbConfig(MongoConfigParam mongoConfigParam) {
        MongoConfigModel mongoConfigModel = DBConfigConvert.paramToModel(mongoConfigParam);
        dbConfigService.mongoConfig(mongoConfigModel);
        LOGGER.info("mongo数据库设置成功\n{}", mongoConfigModel);
    }

    public DBConfigVO showDBConfigs() {
        return DBConfigConvert.modelToView(dbConfigService.mongo(), dbConfigService.redis());
    }

    public DBConfigVO dbConfig(DBConfigParam param) {
        dbConfig(param.getMongo());
        dbConfig(param.getRedis());
        return showDBConfigs();
    }
}
