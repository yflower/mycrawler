package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentConfigModel;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.model.component.DownloadConfigModel;
import com.jal.crawler.web.service.IComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadService")
public class DownloadServiceImpl implements IComponentService {

    @Resource
    private ConfigContext configContext;

    @Override
    public boolean component(ComponentModel componentModel) {
        return configContext.getRpcClient().getClient(componentModel).isPresent();
    }

    @Override
    public boolean config(ComponentConfigModel componentConfigModel) {
        boolean result = false;
        Optional<AbstractComponentClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
        if (clientOptional.isPresent()) {
            result = downloadConfig(clientOptional.get(), (DownloadConfigModel) componentConfigModel);
        }
        return result;

    }

    private boolean downloadConfig(AbstractComponentClient componentClient, DownloadConfigModel configModel) {
        //todo 状态获取失败
        if (componentClient.status().get() != StatusEnum.NO_INIT) {
            //todo 已经设置
            return true;
        }
        return componentClient.setConfig(DownloadConfig.newBuilder()
                .setThread(configModel.getThread())
                .setSleepTime(configModel.getSleepTime())
                .setProxy(configModel.isProxy())
                .addAllProxyAddress(configModel.getProxyAddress() == null ? new ArrayList<String>() : configModel.getProxyAddress())
                .setPersist(DownloadConfig.Persist.REDIS)
                .setRedisConfig(
                        RedisConfig.newBuilder()
                                .setHost(configModel.getRedisConfigModel().getHost())
                                .setPort(configModel.getRedisConfigModel().getPort())
                                .setPassword(configModel.getRedisConfigModel().getPassword())
                                .build()
                )
                .build());
    }
}
