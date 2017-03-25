package com.jal.crawler.web.biz;

import com.jal.crawler.web.convert.DBConfigConvert;
import com.jal.crawler.web.data.param.MongoConfigParam;
import com.jal.crawler.web.data.param.RedisConfigParam;
import com.jal.crawler.web.data.view.DBConfigVO;
import com.jal.crawler.web.service.IDBConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by jal on 2017/2/25.
 */
@Component
public class DBBiz {
    @Resource
    private IDBConfigService dbConfigService;

    public void dbConfig(RedisConfigParam redisConfigParam) throws Exception {
        dbConfigService.redisConfig(DBConfigConvert.paramToModel(redisConfigParam));
    }

    public void dbConfig(MongoConfigParam mongoConfigParam) throws Exception {
        dbConfigService.mongoConfig(DBConfigConvert.paramToModel(mongoConfigParam));
    }

    public DBConfigVO showDBConfigs() {
        return dbConfigService.showDBConfig();
    }
}
