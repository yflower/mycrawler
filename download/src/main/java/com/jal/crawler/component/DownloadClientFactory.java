package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.status.RpcComponentLeaderServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.rpc.client.DownloadInitClient;
import com.jal.crawler.rpc.client.DownloadLeaderClient;
import com.jal.crawler.rpc.client.DownloadStatusClient;
import com.jal.crawler.rpc.client.DownloadTaskClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

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

        RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub leaderStub=RpcComponentLeaderServiceGrpc.newBlockingStub(channel);

        DownloadInitClient initClient = new DownloadInitClient();
        DownloadStatusClient statusClient = new DownloadStatusClient();
        DownloadTaskClient taskClient = new DownloadTaskClient();
        DownloadLeaderClient leaderClient=new DownloadLeaderClient();

        initClient.setStub(initStub);
        initClient.setComponentRelation(componentRelation);
        statusClient.setStub(statusStub);
        statusClient.setComponentRelation(componentRelation);
        statusClient.setComponentContext(downLoadContext);
        taskClient.setStub(taskStub);
        taskClient.setComponentRelation(componentRelation);
        leaderClient.setStub(leaderStub);


        componentClient.initClient = initClient;
        componentClient.taskClient = taskClient;
        componentClient.statusClient = statusClient;
        componentClient.leaderClient=leaderClient;

        boolean tryConnect = componentClient.tryConnect();


        return tryConnect?Optional.of(componentClient):Optional.empty();
    }


}
