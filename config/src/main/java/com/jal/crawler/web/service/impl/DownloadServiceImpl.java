package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.DownloadClient;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.web.adapter.DownloadAdapter;
import com.jal.crawler.web.data.enums.ComponentStatusEnum;
import com.jal.crawler.web.data.exception.AddressNotFound;
import com.jal.crawler.web.data.exception.ComponentNotFoundException;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.model.DownloadConfigModel;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadConfigParam;
import com.jal.crawler.web.data.view.DownloadVO;
import com.jal.crawler.web.service.IDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
@Service
public class DownloadServiceImpl extends DownloadAdapter implements IDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IDownloadService.class);

    @Resource
    private ConfigContext configContext;


    @Override
    public List<DownloadVO> status(List<String> address) {
        if (address == null || address.isEmpty()) {
            return Clients.downs.keySet().stream().map(this::status).collect(Collectors.toList());
        }
        return address.stream().map(this::status).collect(Collectors.toList());
    }

    @Override
    public void component(ComponentParam componentParam) throws AddressNotFound {
        AbstractComponentClient downloadClient = Clients.downloadClient(componentParam.getHost(), componentParam.getPort());

        if (!downloadClient.isConnected()) {
            Clients.close(downloadClient);
            throw new AddressNotFound("cant find address for download component");
        }


    }

    @Override
    public void config(DownloadConfigParam downloadConfigParam) throws ComponentNotFoundException, DBConfigException {
        AbstractComponentClient downloadClient = Clients.downs.get(downloadConfigParam.getHost() + ":" + downloadConfigParam.getPort());

        if (downloadClient == null) {
            throw new ComponentNotFoundException("you must add component before you config it");
        }


        if (configContext.getMongoConfigModel() == null || configContext.getRedisConfigModel() == null) {
            throw new DBConfigException("you must init db before init download component");
        }


        DownloadConfigModel model = paramToModel(downloadConfigParam, configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
        downloadConfig(downloadClient, model);

    }


    private DownloadVO status(String address) {
        DownloadVO downloadVO = new DownloadVO();
        if (Clients.downs.containsKey(address)) {
            DownloadClient client = Clients.downs.get(address);
            downloadVO.setStatus(client.status());
            downloadVO.setAddress(client.address());
            downloadVO.setThread(client.showConfig().getThread());
            downloadVO.setSleepTime(client.showConfig().getSleepTime());
            downloadVO.setProxy(client.showConfig().getProxy());
            downloadVO.setProxyAddress(client.showConfig().getProxyAddressList());
            downloadVO.setTaskNum(client.showTask().size());
            downloadVO.setTasks(client.showTask());
        }
        return downloadVO;
    }


    private void downloadConfig(AbstractComponentClient componentClient, DownloadConfigModel configModel) {
        if (componentClient.status() != ComponentStatusEnum.NO_INIT.toString()) {
            return;
        }
        componentClient.setConfig(DownloadConfig.newBuilder()
                .setThread(configModel.getThread())
                .setSleepTime(configModel.getSleepTime())
                .setProxy(configModel.isProxy())
                .addAllProxyAddress(configModel.getProxyAddress()==null?new ArrayList<String>():configModel.getProxyAddress())
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
