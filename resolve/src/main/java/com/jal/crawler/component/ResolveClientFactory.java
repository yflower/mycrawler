package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.resolve.RpcResolveConfigGrpc;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentLeaderServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.rpc.client.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveClientFactory extends AbstractComponentClientFactory {
    private ResolveContext resolveContext;

    public ResolveClientFactory(ResolveContext resolveContext) {
        this.resolveContext = resolveContext;
    }

    @Override
    public Optional<ComponentClient> create(ComponentRelation componentRelation) {
        ComponentClient componentClient = new ComponentClient();

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getPort())
                .usePlaintext(true)
                .build();
        RpcComponentStatusGrpc.RpcComponentStatusBlockingStub statusStub = RpcComponentStatusGrpc.newBlockingStub(channel);

        RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub leaderStub = RpcComponentLeaderServiceGrpc.newBlockingStub(channel);

        RpcResolveConfigGrpc.RpcResolveConfigBlockingStub configStub = RpcResolveConfigGrpc.newBlockingStub(channel);

        RpcResolveTaskGrpc.RpcResolveTaskBlockingStub taskOpStub = RpcResolveTaskGrpc.newBlockingStub(channel);

        RpcComponentHeartServiceGrpc.RpcComponentHeartServiceBlockingStub heartStub = RpcComponentHeartServiceGrpc.newBlockingStub(channel);

        ResolveInitClient initClient = new ResolveInitClient();
        ResolveStatusClient statusClient = new ResolveStatusClient();
        ResolveLeaderClient leaderClient = new ResolveLeaderClient();
        ResolveTaskClient taskClient = new ResolveTaskClient();
        ResolveHeartClient heartClient = new ResolveHeartClient();

        initClient.setStub(configStub);
        initClient.setComponentRelation(componentRelation);
        statusClient.setStub(statusStub);
        statusClient.setComponentContext(resolveContext);
        statusClient.setComponentRelation(componentRelation);
        leaderClient.setStub(leaderStub);
        leaderClient.setComponentContext(resolveContext);
        leaderClient.setComponentRelation(componentRelation);
        taskClient.setStub(taskOpStub);
        taskClient.setComponentRelation(componentRelation);
        heartClient.setComponentRelation(componentRelation);
        heartClient.setStub(heartStub);


        componentClient.initClient = initClient;
        componentClient.statusClient = statusClient;
        componentClient.leaderClient = leaderClient;
        componentClient.taskClient = taskClient;
        componentClient.heartClient = heartClient;

        Optional<StatusEnum> enumOptional = componentClient.tryConnect();
        boolean tryConnect = enumOptional.isPresent();

        if (tryConnect) {
            componentRelation.setStatus(enumOptional.get());
        }


        return tryConnect ? Optional.of(componentClient) : Optional.empty();
    }
}
