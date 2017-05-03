package com.cufe.taskRpc.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentLeaderClient;
import com.cufe.taskProcessor.task.*;
import com.jal.crawler.proto.status.*;
import com.jal.crawler.proto.status.TaskStatistics;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class RpcLeaderClient extends AbstractComponentLeaderClient<ComponentStatus, ComponentRelation> {
    private RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub stub;

    @Override
    protected ComponentRelation localReqToRpcReq(com.cufe.taskProcessor.component.relation.ComponentRelation componentRelation) {
        ComponentRelation.Builder builder = ComponentRelation.newBuilder()
                .setHost(componentRelation.getHost())
                .setPort(componentRelation.getPort())
                .setStatus(Status.forNumber(componentRelation.getStatus().getCode()))
                .setComponentType(ComponentType.forNumber(componentRelation.getComponentType()))
                .setType(ComponentRelationType.forNumber(componentRelation.getRelationTypeEnum().getCode()));


        ComponentRelation leader = null;
        if (componentRelation.getLeader() != null) {
            leader = ComponentRelation.newBuilder()
                    .setHost(componentRelation.getLeader().getHost())
                    .setPort(componentRelation.getLeader().getPort())
                    .setStatus(Status.forNumber(componentRelation.getLeader().getStatus().getCode()))
                    .setType(ComponentRelationType.forNumber(componentRelation.getLeader().getRelationTypeEnum().getCode()))
                    .build();
            builder.setLeader(leader);
            return builder.build();
        }
        return builder.build();
    }

    @Override
    protected com.cufe.taskProcessor.component.status.ComponentStatus rpcResToLocalRes(ComponentStatus rpcRes) {
        com.cufe.taskProcessor.component.status.ComponentStatus componentStatus = new com.cufe.taskProcessor.component.status.ComponentStatus();
        componentStatus.setComponentStatus(StatusEnum.valueOf(rpcRes.getComponentStatus().name()));
        componentStatus.setPort(rpcRes.getPort());
        componentStatus.setHost(rpcRes.getHost());
        componentStatus.setTasks(
                rpcRes.getTasksMap().entrySet()
                        .stream().map(t -> {
                    LongBinaryOperator function = (l, r) -> l + r;
                    com.cufe.taskProcessor.task.TaskStatistics taskStatistics = new com.cufe.taskProcessor.task.TaskStatistics();
                    taskStatistics.setCyclePerTime(new LongAccumulator(function, t.getValue().getCyclePerTime()));
                    taskStatistics.setResourceFountCycle(new LongAccumulator(function, t.getValue().getResourceFountCycle()));
                    taskStatistics.setResourceNotFoundCycle(new LongAccumulator(function, t.getValue().getResourceNotFoundCycle()));
                    taskStatistics.setProcessorErrorCycle(new LongAccumulator(function, t.getValue().getProcessorErrorCycle()));
                    taskStatistics.setProcessorSuccessCycle(new LongAccumulator(function, t.getValue().getProcessorSuccessCycle()));
                    taskStatistics.setPersistErrorCycle(new LongAccumulator(function, t.getValue().getPersistErrorCycle()));
                    taskStatistics.setPersistSuccessCycle(new LongAccumulator(function, t.getValue().getPersistSuccessCycle()));
                    taskStatistics.setBeginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getStartTime()), ZoneOffset.systemDefault()));
                    taskStatistics.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getEndTime()), ZoneOffset.systemDefault()));
                    taskStatistics.setHistoryStatus(t.getValue().getHistoryStatusMap().<Long, Status>entrySet().stream()
                            .collect(Collectors.toMap(
                                    k -> LocalDateTime.ofInstant(Instant.ofEpochMilli(k.getKey()), ZoneOffset.systemDefault()),
                                    k -> StatusEnum.numberOf(k.getValue().getNumber()))));
                    AbstractTask task = new AbstractTask() {
                        @Override
                        public void init() {

                        }
                    };
                    task.setTaskTag(t.getKey());
                    task.setTaskStatistics(taskStatistics);
                    task.setTest(t.getValue().getTest());
                    task.setStatus(StatusEnum.numberOf(t.getValue().getStatus().getNumber()));
                    return task;
                }).collect(Collectors.toList()));

        return componentStatus;


    }

    @Override
    protected ComponentStatus rpcSend(ComponentRelation rpcReq) {
        return stub.rpcComponentNotify(rpcReq);
    }

    public void setStub(RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub stub) {
        this.stub = stub;
    }

}
