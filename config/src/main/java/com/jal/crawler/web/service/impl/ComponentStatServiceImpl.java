package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.http.AbstractHttpClient;
import com.jal.crawler.http.HttpClientHolder;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.view.componnet.ComponentVO;
import com.jal.crawler.web.service.IComponentStatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
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
            ComponentRelation response = status.get();
            componentVO.setStatus(response.getStatus().name());
            componentVO.setAddress(response.getHost() + ":" + response.getServerPort() + "/" + response.getPort());
            componentVO.setComponentType(response.getComponentType());
            if (response.getLeader() == null) {
                componentVO.setLeaderAddress(componentVO.getAddress());
                componentVO.setLeader(true);
            } else {
                ComponentRelation leader = response.getLeader();
                componentVO.setLeader(false);
                componentVO.setLeaderAddress(leader.getHost() + ":" + leader.getServerPort() + "/" + leader.getPort());
            }
            //todo thread
            return Optional.of(componentVO);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Map<String, Object>> statusWithConfig(ComponentRelation componentRelation) {
        Optional<AbstractHttpClient> optional = configContext.getRpcClient().getClient(componentRelation);
        if (optional.isPresent()) {
            AbstractHttpClient client = optional.get();
            Optional<Map<String, Object>> status = client.statusWithConfig();
            return status;
        }
        return Optional.empty();
    }

}
