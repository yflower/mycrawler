package com.cufe.taskRpc.rpc;

import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.server.AbstractComponentLeaderServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.proto.status.*;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/23.
 */
public class RpcLeaderServer extends RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceImplBase {

    public RpcLeaderServer(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    private ComponentContext componentContext;

    @Override
    public void rpcComponentNotify(ComponentRelation request, StreamObserver<ComponentStatus> responseObserver) {
        RpcLeaderService downloadLeaderServer = new RpcLeaderService(componentContext);
        ComponentStatus status = downloadLeaderServer.receive(request);

        responseObserver.onNext(status);
        responseObserver.onCompleted();


    }

    private class RpcLeaderService extends AbstractComponentLeaderServer<ComponentRelation, ComponentStatus> {

        public RpcLeaderService(ComponentContext componentContext) {
            this.componentContext = componentContext;
        }

        @Override
        protected com.cufe.taskProcessor.component.relation.ComponentRelation rpcResToLocal(ComponentRelation rpcRes) {
            com.cufe.taskProcessor.component.relation.ComponentRelation relation = new com.cufe.taskProcessor.component.relation.ComponentRelation();

            relation.setHost(rpcRes.getHost());
            relation.setPort(rpcRes.getPort());
            relation.setStatus(StatusEnum.numberOf(rpcRes.getStatusValue()));
            relation.setComponentType(rpcRes.getComponentTypeValue());
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
        protected ComponentStatus localToRPC_Q(com.cufe.taskProcessor.component.status.ComponentStatus result) {
            com.jal.crawler.proto.status.ComponentStatus.Builder builder = com.jal.crawler.proto.status.ComponentStatus.newBuilder();
            builder.setComponentStatus(Status.forNumber(result.getComponentStatus().getCode()));
            builder.setHost(result.getHost());
            builder.setPort(result.getPort());
            builder.setComponentType(ComponentType.forNumber(result.getComponentType()));
            if (result.getComponentStatus() == StatusEnum.STARTED) {
                builder.setTaskNum(result.getTasks().size());
                builder.putAllTasks(result.getTasks().stream()
                        .collect(Collectors.toMap(t -> t.getTaskTag(), t -> com.jal.crawler.proto.status.TaskStatistics.newBuilder()
                                .setStatus(Status.forNumber(t.getStatus().getCode()))
                                .setStartTime(t.getTaskStatistics().getBeginTime()
                                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                                .setEndTime(t.getTaskStatistics().getEndTime() == null ? 0 :
                                        t.getTaskStatistics().getEndTime()
                                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                                .setCyclePerTime(t.getTaskStatistics().getCyclePerTime().longValue())
                                .setResourceFountCycle(t.getTaskStatistics().getResourceFountCycle().longValue())
                                .setResourceNotFoundCycle(t.getTaskStatistics().getResourceNotFoundCycle().longValue())
                                .setProcessorErrorCycle(t.getTaskStatistics().getProcessorErrorCycle().longValue())
                                .setProcessorSuccessCycle(t.getTaskStatistics().getProcessorSuccessCycle().longValue())
                                .setPersistErrorCycle(t.getTaskStatistics().getPersistErrorCycle().longValue())
                                .setPersistSuccessCycle(t.getTaskStatistics().getPersistSuccessCycle().longValue())
                                .putAllHistoryStatus(t.getTaskStatistics().getHistoryStatus()
                                        .<LocalDateTime, StatusEnum>entrySet().stream()
                                        .collect(Collectors.toMap(k -> k.getKey().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                                , k -> Status.forNumber(k.getValue().getCode()))))
                                .setTest(t.isTest())

                                .build())));

            }
            return builder.build();
        }


    }
}
