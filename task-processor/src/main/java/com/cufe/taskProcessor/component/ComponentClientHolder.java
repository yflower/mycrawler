package com.cufe.taskProcessor.component;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentStatusClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentClientHolder {
    public static Map<String, ComponentClient> clients = new HashMap<>();

    public static Optional<ComponentClient> from(ComponentRelation relation
            , AbstractComponentInitClient initClient
            , AbstractComponentStatusClient statusClient
            , AbstractComponentTaskClient taskClient) {
        if (clients.containsKey(relation.address())) {
            clients.remove(clients);
        }

        ComponentClient componentClient = new ComponentClient();
        initClient.setComponentRelation(relation);
        statusClient.setComponentRelation(relation);
        taskClient.setComponentRelation(relation);

        if (componentClient.tryConnect()) {
            clients.put(relation.address(), componentClient);
            return Optional.of(componentClient);
        }
        return Optional.empty();

    }

    public static Optional<ComponentClient> from(ComponentRelation relation) {
        if (clients.containsKey(relation.address())) {
            ComponentClient client = clients.get(relation.address());
            if (client.tryConnect()) {
                return Optional.of(clients.get(relation.address()));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }
}
