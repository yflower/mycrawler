package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskRpc.RpcUtils;
import com.jal.crawler.context.DataContext;
import com.jal.crawler.proto.data.RpcDataConfigGrpc;
import com.jal.crawler.proto.data.RpcDataTaskGrpc;
import com.jal.crawler.rpc.client.DataInitClient;
import com.jal.crawler.rpc.client.DataTaskClient;
import com.jal.crawler.rpc.client.DataTaskConfigClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/15.
 */

public class DataClientFactory extends AbstractComponentClientFactory {

    private DataContext dataContext;

    public DataClientFactory(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public Optional<ComponentClient> create(ComponentRelation componentRelation) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getPort())
                .usePlaintext(true)
                .build();

        RpcDataConfigGrpc.RpcDataConfigBlockingStub initStub = RpcDataConfigGrpc.newBlockingStub(channel);

        RpcDataTaskGrpc.RpcDataTaskBlockingStub taskStub = RpcDataTaskGrpc.newBlockingStub(channel);


        DataInitClient initClient = new DataInitClient();
        DataTaskClient taskClient = new DataTaskClient();
        DataTaskConfigClient taskConfigClient=new DataTaskConfigClient();


        initClient.setStub(initStub);
        initClient.setComponentRelation(componentRelation);

        taskClient.setStub(taskStub);
        taskClient.setComponentRelation(componentRelation);
        taskConfigClient.setStub(taskStub);
        taskConfigClient.setComponentRelation(componentRelation);

        return RpcUtils.componentClient(initClient, taskClient, channel, componentRelation, dataContext);

    }


}
