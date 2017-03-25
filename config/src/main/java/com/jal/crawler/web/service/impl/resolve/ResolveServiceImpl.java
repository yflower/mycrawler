package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentConfigModel;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.model.component.ResolveConfigModel;
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


    @Override
    public boolean component(ComponentModel componentModel) {
        return configContext.getRpcClient().getClient(componentModel).isPresent();
    }

    @Override
    public boolean config(ComponentConfigModel componentConfigModel) {
        boolean result = false;
        Optional<AbstractComponentClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
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
