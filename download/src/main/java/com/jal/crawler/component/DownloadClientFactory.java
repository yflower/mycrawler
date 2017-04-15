package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.rpc.client.DownloadInitClient;
import com.jal.crawler.rpc.client.DownloadStatusClient;
import com.jal.crawler.rpc.client.DownloadTaskClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadClientFactory extends AbstractComponentClientFactory {
    @Override
    public Optional<ComponentClient> create(ComponentRelation componentRelation) {
        ComponentClient componentClient = new ComponentClient();

        ManagedChannel statusChannel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getStatusPort())
                .usePlaintext(true)
                .build();

        ManagedChannel initChannel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getInitPort())
                .usePlaintext(true)
                .build();

        ManagedChannel taskChannel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getTaskPort())
                .usePlaintext(true)
                .build();

        RpcComponentStatusGrpc.RpcComponentStatusBlockingStub statusStub = RpcComponentStatusGrpc.newBlockingStub(statusChannel);

        RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub initStub = RpcDownlandConfigGrpc.newBlockingStub(initChannel);

        RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub taskStub = RpcDownloadTaskGrpc.newBlockingStub(taskChannel);

        DownloadInitClient initClient = new DownloadInitClient();
        DownloadStatusClient statusClient = new DownloadStatusClient();
        DownloadTaskClient taskClient = new DownloadTaskClient();

        initClient.setStub(initStub);
        statusClient.setStub(statusStub);
        taskClient.setStub(taskStub);

        componentClient.initClient = initClient;
        componentClient.taskClient = taskClient;
        componentClient.statusClient = statusClient;

        return Optional.of(componentClient);
    }


}
