package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.resolve.RpcResolveConfigGrpc;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.status.RpcComponentLeaderServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.rpc.client.ResolveInitClient;
import com.jal.crawler.rpc.client.ResolveLeaderClient;
import com.jal.crawler.rpc.client.ResolveStatusClient;
import com.jal.crawler.rpc.client.ResolveTaskClient;
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


        ResolveInitClient initClient = new ResolveInitClient();
        ResolveStatusClient statusClient = new ResolveStatusClient();
        ResolveLeaderClient leaderClient = new ResolveLeaderClient();
        ResolveTaskClient taskClient = new ResolveTaskClient();

        initClient.setStub(configStub);
        initClient.setComponentRelation(componentRelation);
        statusClient.setStub(statusStub);
        statusClient.setComponentContext(resolveContext);
        statusClient.setComponentRelation(componentRelation);
        leaderClient.setStub(leaderStub);
        taskClient.setStub(taskOpStub);
        taskClient.setComponentRelation(componentRelation);

        componentClient.initClient = initClient;
        componentClient.statusClient = statusClient;
        componentClient.leaderClient = leaderClient;
        componentClient.taskClient = taskClient;
        return Optional.of(componentClient);
    }
}
