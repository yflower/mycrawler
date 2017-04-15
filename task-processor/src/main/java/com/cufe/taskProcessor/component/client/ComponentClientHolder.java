package com.cufe.taskProcessor.component.client;

import com.cufe.taskProcessor.component.relation.ComponentRelation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentClientHolder {
    private Map<String, ComponentClient> clients = new HashMap<>();

    public Optional<ComponentClient> from(ComponentRelation relation) {
        //尝试从缓存中获取连接client
        if (clients.containsKey(relation.address())) {
            ComponentClient client = clients.get(relation.address());
            //获取之后尝试连接
            if (client.tryConnect()) {
                return Optional.of(clients.get(relation.address()));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    public void remove(ComponentRelation relation) {
        if (clients.containsKey(relation.address())) {
            clients.remove(relation.address());

        }
    }

}
