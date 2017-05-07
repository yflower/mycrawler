package com.jal.crawler.web.convert;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.DownloadConfigRelation;
import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadParam;
import com.jal.crawler.web.data.param.ResolveParam;

/**
 * Created by jal on 2017/2/25.
 */
public class ComponentConvert {


    public static DownloadConfigRelation paramToModel(DownloadParam.downloadSocket param, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        DownloadConfigRelation downloadConfigModel = new DownloadConfigRelation();
        downloadConfigModel.setThread(param.getThread());
        downloadConfigModel.setPort(param.getPort());
        downloadConfigModel.setHost(param.getHost());
        downloadConfigModel.setSleepTime(param.getSleepTime());
        downloadConfigModel.setProxy(param.isProxy());
        downloadConfigModel.setProxyAddress(param.getProxyAddress());
        downloadConfigModel.setRedisConfigModel(redisConfigModel);
        downloadConfigModel.setMongoConfigModel(mongoConfigModel);
        downloadConfigModel.setComponentType(ComponentEnum.DOWNLOAD.getCode());
        return downloadConfigModel;

    }

    public static ResolveConfigRelation paramToModel(ResolveParam.resolveSocket param, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        ResolveConfigRelation resolveConfigModel = new ResolveConfigRelation();
        resolveConfigModel.setThread(param.getThread());
        resolveConfigModel.setPort(param.getPort());
        resolveConfigModel.setHost(param.getHost());
        resolveConfigModel.setRedisConfigModel(redisConfigModel);
        resolveConfigModel.setMongoConfigModel(mongoConfigModel);
        resolveConfigModel.setComponentType(ComponentEnum.RESOLVE.getCode());
        return resolveConfigModel;

    }


    public static ComponentRelation paramToModel(ComponentParam.socket componentParam) {
        return new ComponentRelation(componentParam.getHost(), componentParam.getPort(),componentParam.getServerPort());
    }

    public static ComponentRelation paramToModel(DownloadParam.downloadSocket downloadSocket) {
        return new ComponentRelation(downloadSocket.getHost(), downloadSocket.getPort(),downloadSocket.getServerPort());

    }

    public static ComponentRelation paramToModel(ResolveParam.resolveSocket resolveSocket) {
        return new ComponentRelation(resolveSocket.getHost(), resolveSocket.getPort(),resolveSocket.getServerPort());
    }
}
