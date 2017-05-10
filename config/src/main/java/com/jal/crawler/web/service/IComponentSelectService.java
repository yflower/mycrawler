package com.jal.crawler.web.service;

import com.jal.crawler.web.data.model.component.ComponentRelation;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
public interface IComponentSelectService {
    //组件相关的随机筛选
    Optional<ComponentRelation> selectComponent(List<ComponentRelation> componentRelations);

    //需要使用同一台机器的任务组件筛选
    Optional<ComponentRelation> selectComponent(List<ComponentRelation> componentRelations, String taskTag);
}
