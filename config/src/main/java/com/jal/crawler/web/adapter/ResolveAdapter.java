package com.jal.crawler.web.adapter;

import com.jal.crawler.web.data.model.MongoConfigModel;
import com.jal.crawler.web.data.model.RedisConfigModel;
import com.jal.crawler.web.data.model.ResolveConfigModel;
import com.jal.crawler.web.data.param.ResolveConfigParam;

/**
 * Created by jal on 2017/2/19.
 */
public abstract class ResolveAdapter {
    public ResolveConfigModel paramToModel(ResolveConfigParam param, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        ResolveConfigModel resolveConfigModel = new ResolveConfigModel();
        resolveConfigModel.setThread(param.getThread());
        resolveConfigModel.setPort(param.getPort());
        resolveConfigModel.setHost(param.getHost());
        resolveConfigModel.setRedisConfigModel(redisConfigModel);
        resolveConfigModel.setMongoConfigModel(mongoConfigModel);
        return resolveConfigModel;

    }
}
