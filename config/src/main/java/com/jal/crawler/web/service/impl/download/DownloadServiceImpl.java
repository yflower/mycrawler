package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.DownloadConfigRelation;
import com.jal.crawler.web.service.IComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadService")
public class DownloadServiceImpl implements IComponentService {

    @Resource
    private ConfigContext configContext;

    @Override
    public boolean component(ComponentRelation componentRelation) {
        return configContext.getRpcClient().getClient(componentRelation).isPresent();
    }

    @Override
    public boolean config(ComponentConfigRelation componentConfigModel) {
        boolean result = false;
        Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentConfigModel);
        if (clientOptional.isPresent()) {
            result = downloadConfig(clientOptional.get(), (DownloadConfigRelation) componentConfigModel);
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
