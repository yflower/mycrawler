package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractComponentClient;
import com.jal.crawler.rpc.RpcClient;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.view.ComponentVO;
import com.jal.crawler.web.service.IComponentStatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service
public class ComponentStatServiceImpl implements IComponentStatService {
    @Resource
    private ConfigContext configContext;


    @Override
    public Optional<ComponentEnum> type(ComponentModel componentModel) {
        return RpcClient.tryConnect(componentModel);
    }

    @Override
    public Optional<ComponentVO> status(ComponentModel componentModel) {
        ComponentVO componentVO = new ComponentVO();
        Optional<AbstractComponentClient> optional = configContext.getRpcClient().getClient(componentModel);
        if (optional.isPresent()) {
            AbstractComponentClient client = optional.get();
            Optional<StatusEnum> status = client.status();
            componentVO.setStatus(status.get().name());
            componentVO.setAddress(client.address());
            componentVO.setTaskNum(client.showTask().size());
            componentVO.setTasks(client.showTask());
            //todo thread
            return Optional.of(componentVO);
        }
        return Optional.empty();
    }
}
