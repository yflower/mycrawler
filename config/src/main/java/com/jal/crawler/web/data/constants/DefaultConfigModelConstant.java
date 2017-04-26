package com.jal.crawler.web.data.constants;

import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.DownloadConfigRelation;
import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;

/**
 * Created by jianganlan on 2017/4/3.
 */
public class DefaultConfigModelConstant {
    /**
     * 默认的组件设置
     *
     * @param componentRelation
     * @return 默认的可设置的下载组件
     */
    public static DownloadConfigRelation defaultDownloadConfig(ComponentRelation componentRelation
            , RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        DownloadConfigRelation downloadConfigModel = new DownloadConfigRelation();
        downloadConfigModel.setHost(componentRelation.getHost());
        downloadConfigModel.setPort(componentRelation.getPort());
        downloadConfigModel.setComponentType(componentRelation.getComponentType());
        downloadConfigModel.setThread(2);
        downloadConfigModel.setSleepTime(100);
        downloadConfigModel.setRedisConfigModel(redisConfigModel);
        downloadConfigModel.setMongoConfigModel(mongoConfigModel);
        return downloadConfigModel;
    }

    /**
     * 默认的组件设置
     *
     * @param componentRelation
     * @return 默认可设置的解析组件
     */
    public static ResolveConfigRelation defaultResolveConfig(ComponentRelation componentRelation
            , RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        ResolveConfigRelation resolveConfigModel = new ResolveConfigRelation();
        resolveConfigModel.setComponentType(componentRelation.getComponentType());
        resolveConfigModel.setHost(componentRelation.getHost());
        resolveConfigModel.setPort(componentRelation.getPort());
        resolveConfigModel.setThread(2);
        resolveConfigModel.setRedisConfigModel(redisConfigModel);
        resolveConfigModel.setMongoConfigModel(mongoConfigModel);
        return resolveConfigModel;
    }


}
