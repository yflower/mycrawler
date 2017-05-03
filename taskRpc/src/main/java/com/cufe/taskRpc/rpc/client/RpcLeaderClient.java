package com.cufe.taskRpc.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentLeaderClient;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.proto.status.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        com.cufe.taskProcessor.component.status.ComponentStatus status = new com.cufe.taskProcessor.component.status.ComponentStatus();

        status.setHost(rpcRes.getHost());
        status.setPort(rpcRes.getPort());
        status.setComponentStatus(StatusEnum.numberOf(rpcRes.getComponentStatusValue()));
        status.setComponentType(rpcRes.getComponentTypeValue());
        status.setTasks(
                rpcRes.getTasksMap().entrySet()
                        .stream().map(t -> {
                    //todo 数据没有取完整
                    com.cufe.taskProcessor.task.TaskStatistics taskStatistics = new com.cufe.taskProcessor.task.TaskStatistics();
                    taskStatistics.setResourceTotalCycle(t.getValue().getResourceTotal());
                    taskStatistics.setProcessorTotalCycle(t.getValue().getProcessorTotal());
                    taskStatistics.setPersistTotalCycle(t.getValue().getPersistTotal());
                    taskStatistics.setBeginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getStartTime()), ZoneOffset.systemDefault()));
                    taskStatistics.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getEndTime()), ZoneOffset.systemDefault()));
                    AbstractTask task = new AbstractTask() {
                        @Override
                        public void init() {

                        }
                    };
                    task.setTaskTag(t.getKey());
                    task.setTaskStatistics(taskStatistics);
                    return task;
                }).collect(Collectors.toList()));


        return status;
    }

    @Override
    protected ComponentStatus rpcSend(ComponentRelation rpcReq) {
        return stub.rpcComponentNotify(rpcReq);
    }

    public void setStub(RpcComponentLeaderServiceGrpc.RpcComponentLeaderServiceBlockingStub stub) {
        this.stub = stub;
    }

}
