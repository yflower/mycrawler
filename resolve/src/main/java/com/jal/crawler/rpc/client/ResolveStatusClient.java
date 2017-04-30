package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentStatusClient;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.TaskStatistics;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.ComponentType;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.proto.status.Status;
import com.jal.crawler.task.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveStatusClient extends AbstractComponentStatusClient<ComponentStatus,ComponentStatus> {
    private RpcComponentStatusGrpc.RpcComponentStatusBlockingStub stub;

    @Override
    protected int componentType() {
        return ComponentType.RESOLVE_VALUE;
    }

    @Override
    protected ComponentStatus localReqToRpcReq(com.cufe.taskProcessor.component.status.ComponentStatus componentStatus) {
        com.jal.crawler.proto.status.ComponentStatus.Builder builder = com.jal.crawler.proto.status.ComponentStatus.newBuilder();
        builder.setComponentStatus(Status.forNumber(componentStatus.getComponentStatus().getCode()));
        builder.setHost(componentStatus.getHost());
        builder.setPort(componentStatus.getPort());
        builder.setComponentType(ComponentType.forNumber(componentStatus.getComponentType()));
        if (componentStatus.getComponentStatus() == StatusEnum.STARTED) {
            builder.setTaskNum(componentStatus.getTasks().size());
            builder.putAllTasks(componentStatus.getTasks().stream()
                    .collect(Collectors.toMap(t -> t.getTaskTag(), t -> com.jal.crawler.proto.status.TaskStatistics.newBuilder()
                            .setStatus(Status.forNumber(t.getStatus().getCode()))
                            .setStartTime(t.getTaskStatistics().getBeginTime()
                                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                            .setEndTime(t.getTaskStatistics().getEndTime() == null ? 0 :
                                    t.getTaskStatistics().getEndTime()
                                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                            .setResourceTotal((int) t.getTaskStatistics().getResourceTotalCycle())
                            .setProcessorTotal((int) t.getTaskStatistics().getProcessorTotalCycle())
                            .setPersistTotal((int) t.getTaskStatistics().getPersistTotalCycle())
                            .setTest(t.isTest())

                            .build())));
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
                    //todo 数据没有取完整
                    TaskStatistics taskStatistics = new TaskStatistics();
                    taskStatistics.setResourceTotalCycle(t.getValue().getResourceTotal());
                    taskStatistics.setProcessorTotalCycle(t.getValue().getProcessorTotal());
                    taskStatistics.setPersistTotalCycle(t.getValue().getPersistTotal());
                    taskStatistics.setBeginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getStartTime()), ZoneOffset.systemDefault()));
                    taskStatistics.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getEndTime()), ZoneOffset.systemDefault()));
                    Task task = new Task();
                    task.setTaskTag(t.getKey());
                    task.setTaskStatistics(taskStatistics);
                    task.setTest(t.getValue().getTest());
                    task.setStatus(StatusEnum.numberOf(t.getValue().getStatus().getNumber()));
                    return task;
                }).collect(Collectors.toList()));

        return componentStatus;
    }

    @Override
    protected ComponentStatus rpcSend(ComponentStatus rpcReq) {
        return stub.rpcComponentStatus(rpcReq);
    }
    public void setStub(RpcComponentStatusGrpc.RpcComponentStatusBlockingStub stub) {
        this.stub = stub;
    }

    public void setComponentContext(ResolveContext resolveContext){
        this.componentContext=resolveContext;
    }
}
