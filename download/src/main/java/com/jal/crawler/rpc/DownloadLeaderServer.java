package com.jal.crawler.rpc;

import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.rpc.server.AbstractComponentLeaderServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.status.*;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/23.
 */
@Component
public class DownloadLeaderServer extends RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceImplBase {

    @Autowired
    private DownLoadContext downLoadContext;

    @Override
    public void rpcComponentNotify(ComponentRelation request, StreamObserver<ComponentStatus> responseObserver) {
        DownloadLeaderService downloadLeaderServer = new DownloadLeaderService(downLoadContext);
        ComponentStatus status = downloadLeaderServer.receive(request);

        responseObserver.onNext(status);
        responseObserver.onCompleted();


    }

    private class DownloadLeaderService extends AbstractComponentLeaderServer<DownLoadContext, ComponentRelation, ComponentStatus> {

        public DownloadLeaderService(DownLoadContext componentContext) {
            this.componentContext = componentContext;
        }

        @Override
        protected com.cufe.taskProcessor.component.relation.ComponentRelation rpcResToLocal(ComponentRelation rpcRes) {
            com.cufe.taskProcessor.component.relation.ComponentRelation relation = new com.cufe.taskProcessor.component.relation.ComponentRelation();

            relation.setHost(rpcRes.getHost());
            relation.setPort(rpcRes.getPort());
            relation.setStatus(StatusEnum.numberOf(rpcRes.getStatusValue()));
            relation.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(rpcRes.getTypeValue()));
            if (rpcRes.getType() != ComponentRelationType.LEADER) {
                com.cufe.taskProcessor.component.relation.ComponentRelation leader = new com.cufe.taskProcessor.component.relation.ComponentRelation();

                leader.setHost(rpcRes.getLeader().getHost());
                leader.setPort(rpcRes.getLeader().getPort());
                leader.setStatus(StatusEnum.numberOf(rpcRes.getLeader().getStatusValue()));
                leader.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(rpcRes.getLeader().getTypeValue()));

                relation.setLeader(leader);
            }

            return relation;


        }

        @Override
        protected int componentType() {
            return ComponentType.DOWNLOAD_VALUE;
        }

        @Override
        protected ComponentStatus localToRPC_Q(com.cufe.taskProcessor.component.status.ComponentStatus result) {
            com.cufe.taskProcessor.component.status.ComponentStatus componentStatus = result;
            ComponentStatus.Builder builder = ComponentStatus.newBuilder();
            builder.setComponentStatus(Status.forNumber(componentStatus.getComponentStatus().getCode()));
            builder.setComponentType(ComponentType.forNumber(componentStatus.getComponentType()));
            if (componentStatus.getComponentStatus() == StatusEnum.STARTED) {
                builder.setTaskNum(componentStatus.getTasks().size());
                builder.putAllTasks(componentStatus.getTasks().stream()
                        .collect(Collectors.toMap(t -> t.getTaskTag(), t -> TaskStatistics.newBuilder()
                                .setStatus(Status.forNumber(t.getStatus().getCode()))
                                .setStartTime(t.getTaskStatistics().getBeginTime()
                                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                                .setEndTime(t.getTaskStatistics().getEndTime() == null ? 0 :
                                        t.getTaskStatistics().getEndTime()
                                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                                .setResourceTotal((int) t.getTaskStatistics().getResourceTotalCycle())
                                .setProcessorTotal((int) t.getTaskStatistics().getProcessorTotalCycle())
                                .setPersistTotal((int) t.getTaskStatistics().getPersistTotalCycle())

                                .build())));
            }
            return builder.build();
        }


    }
}
