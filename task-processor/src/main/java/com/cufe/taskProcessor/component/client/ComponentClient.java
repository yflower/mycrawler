package com.cufe.taskProcessor.component.client;

import com.cufe.taskProcessor.rpc.client.*;
import com.cufe.taskProcessor.task.StatusEnum;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentClient {
    public AbstractComponentTaskClient taskClient;

    public AbstractComponentInitClient initClient;

    public AbstractComponentStatusClient statusClient;

    public AbstractComponentLeaderClient leaderClient;

    public AbstractComponentHeartClient heartClient;

    public AbstractComponentTaskConfigClient taskConfigClient;


    public Optional<StatusEnum> tryConnect() {
        try {
            StatusEnum statusEnum = heartClient.heart();
            return Optional.of(statusEnum);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
