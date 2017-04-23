package com.cufe.taskProcessor.component.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentLeaderClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentStatusClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentClient {
    public AbstractComponentTaskClient taskClient;

    public AbstractComponentInitClient initClient;

    public AbstractComponentStatusClient statusClient;

    public AbstractComponentLeaderClient leaderClient;


    public boolean tryConnect() {
        try {
            statusClient.status();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
