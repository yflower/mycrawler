package com.jal.crawler.web.service.impl.data;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ComponentRelationTypeEnum;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.DataConfigRelation;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.IComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by jal on 2017/3/19.
 */
@Service("dataService")
public class DataServiceImpl implements IComponentService {
    private static final Logger LOGGER = Logger.getLogger(DataServiceImpl.class.getSimpleName());

    @Resource
    private ConfigContext configContext;

    @Resource
    private IComponentSelectService componentSelectService;

    @Override
    public boolean component(ComponentRelation componentRelation) {
        return configContext.getRpcClient().getClient(componentRelation).isPresent();
    }

    @Override
    public boolean config(ComponentConfigRelation componentConfigModel) {
        boolean result = false;
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(configContext.downloadComponent());
        //通过其他组件来创建
        if (relationOptional.isPresent()) {
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(relationOptional.get());
            if (clientOptional.isPresent()) {
                result = dataConfig(clientOptional.get(), (DataConfigRelation) componentConfigModel);
            }

        }

        //尝试自身来创建，只有leader才是这种情况
        else {
            LOGGER.warning("设置组件时，没有获取到有效组件，尝试直连组件进行设置");
            LOGGER.warning("注意此时 必须是leader才走这条通道");
            componentConfigModel.setRelationTypeEnum(ComponentRelationTypeEnum.LEADER);
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
            if (clientOptional.isPresent()) {
                result = dataConfig(clientOptional.get(), (DataConfigRelation) componentConfigModel);
            }
        }
        return result;

    }


    private boolean dataConfig(AbstractHttpClient componentClient, DataConfigRelation configModel) {
        Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(configModel);
        if (!clientOptional.isPresent()) {
            LOGGER.warning("SERVER:设置组件时，组件不可连接");
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "设置组件时，组件不可连接");
        }
        Optional<ComponentRelation> optional = clientOptional.get().status();
        if (!optional.isPresent()) {
            LOGGER.warning("SERVER:设置组件时，组件不可连接");
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "SERVER:设置组件时，组件不可连接");
        }
        StatusEnum status = optional.get().getStatus();
        //todo 状态获取失败
        if (status != StatusEnum.NO_INIT) {
            //todo 已经设置
            return true;
        }
        return componentClient.setConfig(configModel);
    }
}
