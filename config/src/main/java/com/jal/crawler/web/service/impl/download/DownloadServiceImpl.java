package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.DownloadClient;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.ComponentConfigModel;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.model.configModel.DownloadConfigModel;
import com.jal.crawler.web.data.view.ComponentVO;
import com.jal.crawler.web.data.view.DownloadVO;
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

    public Optional<ComponentVO> status(ComponentModel componentModel) {
        DownloadVO downloadVO = new DownloadVO();
        Optional<AbstractComponentClient> optional = configContext.getClients().getClient(componentModel);
        if (optional.isPresent()) {
            DownloadClient client = (DownloadClient) optional.get();
            downloadVO.setStatus(client.status().name());
            downloadVO.setAddress(client.address());
            downloadVO.setThread(client.showConfig().getThread());
            downloadVO.setSleepTime(client.showConfig().getSleepTime());
            downloadVO.setProxy(client.showConfig().getProxy());
            downloadVO.setProxyAddress(client.showConfig().getProxyAddressList());
            downloadVO.setTaskNum(client.showTask().size());
            downloadVO.setTasks(client.showTask());
            return Optional.of(downloadVO);
        }
        return Optional.empty();
    }

    @Override
    public boolean component(ComponentModel componentModel) {
        return configContext.getClients().getClient(componentModel).isPresent();
    }

    @Override
    public boolean config(ComponentConfigModel componentConfigModel) {
        boolean result = false;
        Optional<AbstractComponentClient> clientOptional = configContext.getClients().getClient(componentConfigModel);
        if (clientOptional.isPresent()) {
            result = downloadConfig(clientOptional.get(), (DownloadConfigModel) componentConfigModel);
        }
        return result;

    }

    private boolean downloadConfig(AbstractComponentClient componentClient, DownloadConfigModel configModel) {
        if (componentClient.status() != StatusEnum.NO_INIT) {
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
