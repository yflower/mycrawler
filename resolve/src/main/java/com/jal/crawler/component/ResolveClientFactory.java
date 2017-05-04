package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskRpc.RpcUtils;
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

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getPort())
                .usePlaintext(true)
                .build();

        RpcResolveConfigGrpc.RpcResolveConfigBlockingStub configStub = RpcResolveConfigGrpc.newBlockingStub(channel);

        RpcResolveTaskGrpc.RpcResolveTaskBlockingStub taskOpStub = RpcResolveTaskGrpc.newBlockingStub(channel);



        ResolveInitClient initClient = new ResolveInitClient();

        ResolveTaskClient taskClient = new ResolveTaskClient();

        ResolveTaskConfigClient taskConfigClient=new ResolveTaskConfigClient();

        initClient.setStub(configStub);
        initClient.setComponentRelation(componentRelation);
        taskClient.setStub(taskOpStub);
        taskClient.setComponentRelation(componentRelation);
        taskConfigClient.setStub(taskOpStub);
        taskConfigClient.setComponentRelation(componentRelation);

        return RpcUtils.componentClient(initClient,taskClient,channel,componentRelation,resolveContext);
    }
}
