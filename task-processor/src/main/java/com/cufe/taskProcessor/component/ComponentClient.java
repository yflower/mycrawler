package com.cufe.taskProcessor.component;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentStatusClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentClient {
    AbstractComponentTaskClient taskClient;

    AbstractComponentInitClient initClient;

    AbstractComponentStatusClient statusClient;


    public boolean tryConnect() {
        try {
            statusClient.status();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
