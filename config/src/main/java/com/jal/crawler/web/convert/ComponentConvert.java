package com.jal.crawler.web.convert;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.model.configModel.DownloadConfigModel;
import com.jal.crawler.web.data.model.configModel.ResolveConfigModel;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadParam;
import com.jal.crawler.web.data.param.ResolveParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/25.
 */
public class ComponentConvert {


    public static DownloadConfigModel paramToModel(DownloadParam.downloadSocket param, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        DownloadConfigModel downloadConfigModel = new DownloadConfigModel();
        downloadConfigModel.setThread(param.getThread());
        downloadConfigModel.setPort(param.getPort());
        downloadConfigModel.setHost(param.getHost());
        downloadConfigModel.setSleepTime(param.getSleepTime());
        downloadConfigModel.setProxy(param.isProxy());
        downloadConfigModel.setProxyAddress(param.getProxyAddress());
        downloadConfigModel.setRedisConfigModel(redisConfigModel);
        downloadConfigModel.setMongoConfigModel(mongoConfigModel);
        downloadConfigModel.setComponentEnum(ComponentEnum.DOWNLOAD);
        return downloadConfigModel;

    }

    public static ResolveConfigModel paramToModel(ResolveParam.resolveSocket param, RedisConfigModel redisConfigModel, MongoConfigModel mongoConfigModel) {
        ResolveConfigModel resolveConfigModel = new ResolveConfigModel();
        resolveConfigModel.setThread(param.getThread());
        resolveConfigModel.setPort(param.getPort());
        resolveConfigModel.setHost(param.getHost());
        resolveConfigModel.setRedisConfigModel(redisConfigModel);
        resolveConfigModel.setMongoConfigModel(mongoConfigModel);
        resolveConfigModel.setComponentEnum(ComponentEnum.RESOLVE);
        return resolveConfigModel;

    }


    public static ComponentModel paramToModel(ComponentParam.socket componentParam) {
        return new ComponentModel(componentParam.getHost(),componentParam.getPort());
    }

    public static ComponentModel paramToModel(DownloadParam.downloadSocket downloadSocket) {
        return new ComponentModel(downloadSocket.getHost(),downloadSocket.getPort());

    }

    public static ComponentModel paramToModel(ResolveParam.resolveSocket resolveSocket) {
        return new ComponentModel(resolveSocket.getHost(),resolveSocket.getPort());
    }
}
