package com.jal.crawler.web.data.constants;

import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.model.component.DownloadConfigModel;
import com.jal.crawler.web.data.model.component.ResolveConfigModel;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;

/**
 * Created by jianganlan on 2017/4/3.
 */
public class DefaultConfigModelConstant {
    /**
     * 默认的组件设置
     *
     * @param componentModel
     * @return 默认的可设置的下载组件
     */
    public static DownloadConfigModel defaultDownloadConfig(ComponentModel componentModel
            , RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        DownloadConfigModel downloadConfigModel = new DownloadConfigModel();
        downloadConfigModel.setHost(componentModel.getHost());
        downloadConfigModel.setPort(componentModel.getPort());
        downloadConfigModel.setComponentEnum(componentModel.getComponentEnum());
        downloadConfigModel.setThread(2);
        downloadConfigModel.setSleepTime(100);
        downloadConfigModel.setRedisConfigModel(redisConfigModel);
        downloadConfigModel.setMongoConfigModel(mongoConfigModel);
        return downloadConfigModel;
    }

    /**
     * 默认的组件设置
     *
     * @param componentModel
     * @return 默认可设置的解析组件
     */
    public static ResolveConfigModel defaultResolveConfig(ComponentModel componentModel
            , RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        ResolveConfigModel resolveConfigModel = new ResolveConfigModel();
        resolveConfigModel.setComponentEnum(componentModel.getComponentEnum());
        resolveConfigModel.setHost(componentModel.getHost());
        resolveConfigModel.setPort(componentModel.getPort());
        resolveConfigModel.setThread(2);
        resolveConfigModel.setRedisConfigModel(redisConfigModel);
        resolveConfigModel.setMongoConfigModel(mongoConfigModel);
        return resolveConfigModel;
    }


}
