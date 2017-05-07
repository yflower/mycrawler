package com.jal.crawler.web.data.constants;

import com.jal.crawler.web.data.enums.ComponentRelationTypeEnum;
import com.jal.crawler.web.data.model.component.*;
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
        downloadConfigModel.setServerPort(componentRelation.getServerPort());
        downloadConfigModel.setComponentType(componentRelation.getComponentType());
        downloadConfigModel.setThread(2);
        downloadConfigModel.setSleepTime(100);
        downloadConfigModel.setRedisConfigModel(redisConfigModel);
        downloadConfigModel.setMongoConfigModel(mongoConfigModel);
        downloadConfigModel.setRelationTypeEnum(ComponentRelationTypeEnum.CLUSTER);
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
        resolveConfigModel.setServerPort(componentRelation.getServerPort());
        resolveConfigModel.setThread(2);
        resolveConfigModel.setRedisConfigModel(redisConfigModel);
        resolveConfigModel.setMongoConfigModel(mongoConfigModel);
        resolveConfigModel.setRelationTypeEnum(ComponentRelationTypeEnum.CLUSTER);
        return resolveConfigModel;
    }

    public static DataConfigRelation defaultDataConfig(ComponentRelation componentRelation
            , RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel){
        DataConfigRelation dataConfigRelation = new DataConfigRelation();
        dataConfigRelation.setComponentType(componentRelation.getComponentType());
        dataConfigRelation.setHost(componentRelation.getHost());
        dataConfigRelation.setPort(componentRelation.getPort());
        dataConfigRelation.setServerPort(componentRelation.getServerPort());
        dataConfigRelation.setThread(2);
        dataConfigRelation.setRedisConfigModel(redisConfigModel);
        dataConfigRelation.setMongoConfigModel(mongoConfigModel);
        dataConfigRelation.setRelationTypeEnum(ComponentRelationTypeEnum.CLUSTER);
        return dataConfigRelation;
    }


    public static ComponentConfigRelation defaultLinkConfig(ComponentRelation componentRelation, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        LinkConfigRelation linkConfigRelation = new LinkConfigRelation();
        linkConfigRelation.setComponentType(componentRelation.getComponentType());
        linkConfigRelation.setHost(componentRelation.getHost());
        linkConfigRelation.setPort(componentRelation.getPort());
        linkConfigRelation.setServerPort(componentRelation.getServerPort());
        linkConfigRelation.setThread(2);
        linkConfigRelation.setRedisConfigModel(redisConfigModel);
        linkConfigRelation.setMongoConfigModel(mongoConfigModel);

        linkConfigRelation.setRelationTypeEnum(ComponentRelationTypeEnum.CLUSTER);
        return linkConfigRelation;
    }
}
