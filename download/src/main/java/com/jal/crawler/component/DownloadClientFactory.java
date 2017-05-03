package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskRpc.RpcUtils;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentLeaderServiceGrpc;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.rpc.client.DownloadInitClient;
import com.jal.crawler.rpc.client.DownloadTaskClient;
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

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getPort())
                .usePlaintext(true)
                .build();

        RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub initStub = RpcDownlandConfigGrpc.newBlockingStub(channel);

        RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub taskStub = RpcDownloadTaskGrpc.newBlockingStub(channel);

        DownloadInitClient initClient = new DownloadInitClient();
        DownloadTaskClient taskClient = new DownloadTaskClient();

        initClient.setStub(initStub);
        initClient.setComponentRelation(componentRelation);
        taskClient.setStub(taskStub);
        taskClient.setComponentRelation(componentRelation);

        return RpcUtils.componentClient(initClient, taskClient, channel, componentRelation, downLoadContext);
    }


}
