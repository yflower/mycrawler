package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.ResolveClient;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.web.adapter.ResolveAdapter;
import com.jal.crawler.web.data.enums.ComponentStatusEnum;
import com.jal.crawler.web.data.exception.AddressNotFound;
import com.jal.crawler.web.data.exception.ComponentNotFoundException;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.model.ResolveConfigModel;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.ResolveConfigParam;
import com.jal.crawler.web.data.view.ResolveVO;
import com.jal.crawler.web.service.IResolveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/8.
 */
@Service
public class ResolveServiceImpl extends ResolveAdapter implements IResolveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IResolveService.class);

    @Resource
    private ConfigContext configContext;


    @Override
    public List<ResolveVO> status(List<String> address) {
        if (address == null || address.isEmpty()) {
            return Clients.resolves.keySet().stream().map(this::status).collect(Collectors.toList());
        }
        return address.stream().map(this::status).collect(Collectors.toList());
    }

    @Override
    public void component(ComponentParam componentParam) throws AddressNotFound, DBConfigException {
        AbstractComponentClient resolveClient = Clients.resolveClient(componentParam.getHost(), componentParam.getPort());
        if (!resolveClient.isConnected()) {
            Clients.close(resolveClient);
            throw new AddressNotFound("can't find address");
        }

    }

    @Override
    public void config(ResolveConfigParam resolveConfigParam) throws ComponentNotFoundException, DBConfigException {
        AbstractComponentClient resolveClient = Clients.resolves.get(resolveConfigParam.getHost() + ":" + resolveConfigParam.getPort());

        if (resolveClient == null) {
            throw new ComponentNotFoundException("you must add component before you config it");
        }


        if (configContext.getMongoConfigModel() == null || configContext.getRedisConfigModel() == null) {
            throw new DBConfigException("you must init db before init resolve component");
        }


        ResolveConfigModel model = paramToModel(resolveConfigParam, configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
        resolveConfig(resolveClient, model);
    }

    private void resolveConfig(AbstractComponentClient componentClient, ResolveConfigModel configModel) {
        if (componentClient.status() != ComponentStatusEnum.NO_INIT.toString()) {
            return;
        }
        componentClient.setConfig(ResolveConfig.newBuilder()
                .setMongoConfig(
                        ResolveConfig.MongoConfig.newBuilder()
                                .setDatabase(configModel.getMongoConfigModel().getDatabase())
                                .setHost(configModel.getMongoConfigModel().getHost())
                                .setPort(configModel.getMongoConfigModel().getPort())
                                .setUser(configModel.getMongoConfigModel().getUser())
                                .setPassword(configModel.getMongoConfigModel().getPassword())
                                .build()
                )
                .setPersist(ResolveConfig.Persist.MONGO)
                .setThread(configModel.getThread())
                .setRedisConfig(
                        RedisConfig.newBuilder()
                                .setHost(configModel.getRedisConfigModel().getHost())
                                .setPort(configModel.getRedisConfigModel().getPort())
                                .setPassword(configModel.getRedisConfigModel().getPassword())
                                .build()
                )
                .build()
        );
    }


    private ResolveVO status(String address) {
        ResolveVO resolveStatus = new ResolveVO();
        if (Clients.resolves.containsKey(address)) {
            ResolveClient client = Clients.resolves.get(address);
            resolveStatus.setStatus(client.status());
            resolveStatus.setAddress(client.address());
            resolveStatus.setThread(client.showConfig().getThread());
            resolveStatus.setTaskNum(client.showTask().size());
            resolveStatus.setTasks(client.showTask());
        }
        return resolveStatus;
    }
}
