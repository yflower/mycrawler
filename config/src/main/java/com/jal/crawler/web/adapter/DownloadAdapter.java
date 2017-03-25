package com.jal.crawler.web.adapter;


import com.jal.crawler.web.data.model.component.DownloadConfigModel;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import com.jal.crawler.web.data.param.DownloadConfigParam;

/**
 * Created by jal on 2017/2/19.
 */
public abstract class DownloadAdapter {
    public DownloadConfigModel paramToModel(DownloadConfigParam param, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        DownloadConfigModel downloadConfigModel = new DownloadConfigModel();
        downloadConfigModel.setThread(param.getThread());
        downloadConfigModel.setPort(param.getPort());
        downloadConfigModel.setHost(param.getHost());
        downloadConfigModel.setSleepTime(param.getSleepTime());
        downloadConfigModel.setProxy(param.isProxy());
        downloadConfigModel.setProxyAddress(param.getProxyAddress());
        downloadConfigModel.setRedisConfigModel(redisConfigModel);
        downloadConfigModel.setMongoConfigModel(mongoConfigModel);
        return downloadConfigModel;

    }
}
