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
            return Optional.of(clients.get(relation.address()));
        }
        return Optional.empty();
    }

    public void remove(ComponentRelation relation) {
        if (clients.containsKey(relation.address())) {
            clients.remove(relation.address());

        }
    }

    public void add(ComponentRelation relation, ComponentClient client) {
        if (client.tryConnect()) {
            clients.put(relation.address(), client);
        }
    }

}
