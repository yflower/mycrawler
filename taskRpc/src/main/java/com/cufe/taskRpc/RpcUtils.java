package com.cufe.taskRpc;

import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;
import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskRpc.rpc.client.RpcHeartClient;
import com.cufe.taskRpc.rpc.client.RpcLeaderClient;
import com.cufe.taskRpc.rpc.client.RpcStatusClient;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentLeaderServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ServerBuilder;
import com.cufe.taskRpc.rpc.RpcHeartServer;
import com.cufe.taskRpc.rpc.RpcLeaderServer;
import com.cufe.taskRpc.rpc.RpcStatusServer;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class RpcUtils {
    public static ServerBuilder registServer(ServerBuilder serverBuilder, ComponentContext componentContext) {
        RpcHeartServer rpcHeartServer = new RpcHeartServer(componentContext);
        RpcLeaderServer rpcLeaderServer = new RpcLeaderServer(componentContext);
        RpcStatusServer rpcStatusServer = new RpcStatusServer(componentContext);
        return serverBuilder.addService(rpcHeartServer)
                .addService(rpcLeaderServer)
                .addService(rpcStatusServer);
    }

    public static Optional<ComponentClient> componentClient(AbstractComponentInitClient initClient,AbstractComponentTaskClient taskClient,
            AbstractComponentTaskConfigClient taskConfigClient,
            ManagedChannel channel, ComponentRelation componentRelation, ComponentContext componentContext){
        ComponentClient componentClient = new ComponentClient();


        RpcComponentStatusGrpc.RpcComponentStatusBlockingStub statusStub = RpcComponentStatusGrpc.newBlockingStub(channel);

        RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub leaderStub = RpcComponentLeaderServiceGrpc.newBlockingStub(channel);

        RpcComponentHeartServiceGrpc.RpcComponentHeartServiceBlockingStub heartStub = RpcComponentHeartServiceGrpc.newBlockingStub(channel);

        RpcStatusClient statusClient = new RpcStatusClient();
        RpcLeaderClient leaderClient = new RpcLeaderClient();
        RpcHeartClient heartClient = new RpcHeartClient();

        statusClient.setStub(statusStub);
        statusClient.setComponentRelation(componentRelation);
        statusClient.setComponentContext(componentContext);
        leaderClient.setStub(leaderStub);
        leaderClient.setComponentRelation(componentRelation);
        leaderClient.setComponentContext(componentContext);
        heartClient.setComponentRelation(componentRelation);
        heartClient.setStub(heartStub);
        heartClient.setComponentContext(componentContext);
        componentClient.statusClient = statusClient;
        componentClient.leaderClient = leaderClient;
        componentClient.heartClient = heartClient;
        componentClient.initClient=initClient;
        componentClient.taskClient=taskClient;
        componentClient.taskConfigClient=taskConfigClient;

        Optional<StatusEnum> enumOptional = componentClient.tryConnect();
        boolean tryConnect = enumOptional.isPresent();

        if (tryConnect) {
            componentRelation.setStatus(enumOptional.get());
        }


        return tryConnect ? Optional.of(componentClient) : Optional.empty();
    }
}
