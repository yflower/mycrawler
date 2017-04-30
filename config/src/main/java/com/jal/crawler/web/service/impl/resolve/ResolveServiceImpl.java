package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.service.IComponentSelectService;
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

    @Resource
    private IComponentSelectService componentSelectService;


    @Override
    public boolean component(ComponentRelation componentRelation) {
        return configContext.getRpcClient().getClient(componentRelation).isPresent();
    }

    @Override
    public boolean config(ComponentConfigRelation componentConfigModel) {
        boolean result = false;
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(configContext.resolveComponent());
        //通过其他组件来创建
        if (relationOptional.isPresent()) {
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(relationOptional.get());
            if (clientOptional.isPresent()) {
                result = resolveConfig(clientOptional.get(), (ResolveConfigRelation) componentConfigModel);
            }

        }
        //尝试自身来创建
        else {
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
            if (clientOptional.isPresent()) {
                result = resolveConfig(clientOptional.get(), (ResolveConfigRelation) componentConfigModel);
            }
        }
        return result;
    }

    private boolean resolveConfig(AbstractHttpClient componentClient, ResolveConfigRelation configModel) {

        //todo  已经设置的情况下 状态获取失败
        if (((ComponentRelation) componentClient.status().get()).getStatus() != StatusEnum.NO_INIT) {
            return true;
        }
        return componentClient.setConfig(configModel);
    }
}
