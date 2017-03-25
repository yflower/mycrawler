package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.ResolveClient;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.ComponentConfigModel;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.model.configModel.ResolveConfigModel;
import com.jal.crawler.web.data.view.ComponentVO;
import com.jal.crawler.web.data.view.ResolveVO;
import com.jal.crawler.web.service.IComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service("resolveService")
public class ResolveServiceImpl implements IComponentService {
    @Resource
    private ConfigContext configContext;


    public Optional<ComponentVO> status(ComponentModel componentModel) {
        ResolveVO resolveStatus = new ResolveVO();
        Optional<AbstractComponentClient> optional = configContext.getClients().getClient(componentModel);
        if (optional.isPresent()) {
            ResolveClient client = (ResolveClient) optional.get();
            resolveStatus.setStatus(client.status().name());
            resolveStatus.setAddress(client.address());
            resolveStatus.setThread(client.showConfig().getThread());
            resolveStatus.setTaskNum(client.showTask().size());
            resolveStatus.setTasks(client.showTask());
            return Optional.of(resolveStatus);
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
            result = resolveConfig(clientOptional.get(), (ResolveConfigModel) componentConfigModel);
        }
        return result;
    }

    private boolean resolveConfig(AbstractComponentClient componentClient, ResolveConfigModel configModel) {
        //todo  已经设置的情况下
        if (componentClient.status() != StatusEnum.NO_INIT) {
            return true;
        }
        return componentClient.setConfig(ResolveConfig.newBuilder()
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
}
