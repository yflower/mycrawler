package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentLeaderServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.rpc.client.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/15.
 */

public class DownloadClientFactory extends AbstractComponentClientFactory {

    private DownLoadContext downLoadContext;

    public DownloadClientFactory(DownLoadContext downLoadContext) {
        this.downLoadContext = downLoadContext;
    }

    @Override
    public Optional<ComponentClient> create(ComponentRelation componentRelation) {
        ComponentClient componentClient = new ComponentClient();

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getPort())
                .usePlaintext(true)
                .build();
        RpcComponentStatusGrpc.RpcComponentStatusBlockingStub statusStub = RpcComponentStatusGrpc.newBlockingStub(channel);

        RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub initStub = RpcDownlandConfigGrpc.newBlockingStub(channel);

        RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub taskStub = RpcDownloadTaskGrpc.newBlockingStub(channel);

        RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub leaderStub = RpcComponentLeaderServiceGrpc.newBlockingStub(channel);

        RpcComponentHeartServiceGrpc.RpcComponentHeartServiceBlockingStub heartStub = RpcComponentHeartServiceGrpc.newBlockingStub(channel);

        DownloadInitClient initClient = new DownloadInitClient();
        DownloadStatusClient statusClient = new DownloadStatusClient();
        DownloadTaskClient taskClient = new DownloadTaskClient();
        DownloadLeaderClient leaderClient = new DownloadLeaderClient();
        DownloadHeartClient heartClient = new DownloadHeartClient();

        initClient.setStub(initStub);
        initClient.setComponentRelation(componentRelation);
        statusClient.setStub(statusStub);
        statusClient.setComponentRelation(componentRelation);
        statusClient.setComponentContext(downLoadContext);
        taskClient.setStub(taskStub);
        taskClient.setComponentRelation(componentRelation);
        leaderClient.setStub(leaderStub);
        leaderClient.setComponentRelation(componentRelation);
        heartClient.setComponentRelation(componentRelation);
        heartClient.setStub(heartStub);


        componentClient.initClient = initClient;
        componentClient.taskClient = taskClient;
        componentClient.statusClient = statusClient;
        componentClient.leaderClient = leaderClient;
        componentClient.heartClient = heartClient;

        Optional<StatusEnum> enumOptional = componentClient.tryConnect();
        boolean tryConnect = enumOptional.isPresent();

        if (tryConnect) {
            componentRelation.setStatus(enumOptional.get());
        }


        return tryConnect ? Optional.of(componentClient) : Optional.empty();
    }


}
