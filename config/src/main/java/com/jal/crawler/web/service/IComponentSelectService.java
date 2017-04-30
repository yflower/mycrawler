package com.jal.crawler.web.service;

import com.jal.crawler.proto.status.Component;
import com.jal.crawler.web.data.model.component.ComponentRelation;

import java.util.List;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
public interface IComponentSelectService {
    Optional<ComponentRelation> selectComponent(List<ComponentRelation> componentRelations);
}
