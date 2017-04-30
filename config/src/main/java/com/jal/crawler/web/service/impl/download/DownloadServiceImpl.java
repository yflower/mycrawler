package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.DownloadConfigRelation;
import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.IComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadService")
public class DownloadServiceImpl implements IComponentService {
    private static final Logger LOGGER=Logger.getLogger(DownloadServiceImpl.class.getSimpleName());

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
                result = downloadConfig(clientOptional.get(), (DownloadConfigRelation) componentConfigModel);
            }

        }

        //尝试自身来创建
        else {
            LOGGER.warning("设置组件时，没有获取到有效组件，尝试直连组件进行设置");
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
            if (clientOptional.isPresent()) {
                result = downloadConfig(clientOptional.get(), (DownloadConfigRelation) componentConfigModel);
            }
        }
        return result;

    }

    private boolean downloadConfig(AbstractHttpClient componentClient, DownloadConfigRelation configModel) {
        //todo 状态获取失败
        if (((ComponentRelation) componentClient.status().get()).getStatus() != StatusEnum.NO_INIT) {
            //todo 已经设置
            return true;
        }
        return componentClient.setConfig(configModel);
    }
}
