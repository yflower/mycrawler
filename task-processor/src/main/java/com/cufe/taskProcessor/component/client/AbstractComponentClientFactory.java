package com.cufe.taskProcessor.component.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/15.
 */
public abstract class AbstractComponentClientFactory {
    //组件客户端的抽象工厂方法
    public abstract Optional<ComponentClient> create(ComponentRelation componentRelation);
}
