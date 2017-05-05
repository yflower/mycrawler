package com.jal.crawler.component;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskRpc.RpcUtils;
import com.jal.crawler.context.LinkContext;
import com.jal.crawler.proto.link.RpcLinkConfigGrpc;
import com.jal.crawler.proto.link.RpcLinkTaskGrpc;
import com.jal.crawler.rpc.client.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Optional;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class LinkClientFactory extends AbstractComponentClientFactory {
    private LinkContext linkContext;

    public LinkClientFactory(LinkContext linkContext) {
        this.linkContext = linkContext;
    }

    @Override
    public Optional<ComponentClient> create(ComponentRelation componentRelation) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentRelation.getHost(), componentRelation.getPort())
                .usePlaintext(true)
                .build();

        RpcLinkConfigGrpc.RpcLinkConfigBlockingStub configStub = RpcLinkConfigGrpc.newBlockingStub(channel);

        RpcLinkTaskGrpc.RpcLinkTaskBlockingStub taskOpStub = RpcLinkTaskGrpc.newBlockingStub(channel);



        LinkInitClient initClient = new LinkInitClient();

        LinkTaskClient taskClient = new LinkTaskClient();

        LinkTaskConfigClient taskConfigClient=new LinkTaskConfigClient();

        initClient.setStub(configStub);
        initClient.setComponentRelation(componentRelation);
        taskClient.setStub(taskOpStub);
        taskClient.setComponentRelation(componentRelation);
        taskConfigClient.setStub(taskOpStub);
        taskConfigClient.setComponentRelation(componentRelation);

        return RpcUtils.componentClient(initClient,taskClient,taskConfigClient,channel,componentRelation,linkContext);
    }
}
