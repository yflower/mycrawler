package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.http.AbstractHttpClient;
import com.jal.crawler.http.HttpClientHolder;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.view.componnet.ComponentVO;
import com.jal.crawler.web.data.view.task.TaskStatusVO;
import com.jal.crawler.web.service.IComponentStatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service
public class ComponentStatServiceImpl implements IComponentStatService {

    @Resource
    private ConfigContext configContext;


    @Override
    public Optional<ComponentEnum> type(ComponentRelation componentRelation) {
        return HttpClientHolder.tryConnect(componentRelation);
    }

    @Override
    public Optional<ComponentVO> status(ComponentRelation componentRelation) {
        ComponentVO componentVO = new ComponentVO();
        Optional<AbstractHttpClient> optional = configContext.getRpcClient().getClient(componentRelation);
        if (optional.isPresent()) {
            AbstractHttpClient client = optional.get();
            Optional<ComponentRelation> status = client.status();
            componentVO.setStatus(status.get().getStatus().name());
            componentVO.setAddress(client.getComponentRelation().getHost());
            //todo thread
            return Optional.of(componentVO);
        }
        return Optional.empty();
    }

    @Override
    public Optional<TaskStatusVO> taskStatus(String taskTag) {
        List<ComponentRelation> componentRelations = configContext.resolveComponent();
        HttpClientHolder rpcClient = configContext.getRpcClient();
        TaskStatusVO statusVO = new TaskStatusVO();

        return null;
    }
}
