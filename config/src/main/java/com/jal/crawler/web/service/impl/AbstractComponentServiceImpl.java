package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.http.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ComponentRelationTypeEnum;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.IComponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/8.
 */
public abstract class AbstractComponentServiceImpl implements IComponentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractComponentServiceImpl.class);

    protected ConfigContext configContext;

    protected IComponentSelectService componentSelectService;

    @Override
    public boolean component(ComponentRelation componentRelation) {
        return configContext.getRpcClient().getClient(componentRelation).isPresent();
    }

    @Override
    public boolean config(ComponentConfigRelation componentConfigModel) {
        boolean result = false;
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(availableComponents());
        //通过其他组件来创建
        if (relationOptional.isPresent()) {
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(relationOptional.get());
            if (clientOptional.isPresent()) {
                result = config(clientOptional.get(), componentConfigModel);
            }

        }

        //尝试自身来创建，只有leader才是这种情况
        else {
            LOGGER.warn("设置组件时，没有获取到有效组件，尝试直连组件进行设置");
            LOGGER.warn("注意此时 必须是leader才走这条通道");
            componentConfigModel.setRelationTypeEnum(ComponentRelationTypeEnum.LEADER);
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
            if (clientOptional.isPresent()) {
                result = config(clientOptional.get(), componentConfigModel);
            }
        }
        return result;
    }

    private boolean config(AbstractHttpClient componentClient, ComponentConfigRelation configModel) {
        Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(configModel);
        if (!clientOptional.isPresent()) {
            LOGGER.warn("SERVER:设置组件时，组件不可连接");
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "设置组件时，组件不可连接");
        }
        Optional<ComponentRelation> optional = clientOptional.get().status();
        if (!optional.isPresent()) {
            LOGGER.warn("SERVER:设置组件时，组件不可连接");
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

    protected abstract List<ComponentRelation> availableComponents();
}
