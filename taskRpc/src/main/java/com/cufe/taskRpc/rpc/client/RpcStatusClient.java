package com.cufe.taskRpc.rpc.client;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.client.AbstractComponentStatusClient;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.TaskStatistics;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.ComponentType;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.proto.status.Status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class RpcStatusClient extends AbstractComponentStatusClient<ComponentStatus, ComponentStatus> {
    private RpcComponentStatusGrpc.RpcComponentStatusBlockingStub stub;


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
                            .setCyclePerTime(t.getTaskStatistics().getCyclePerTime().longValue())
                            .setResourceFountCycle(t.getTaskStatistics().getResourceFountCycle().longValue())
                            .setResourceNotFoundCycle(t.getTaskStatistics().getResourceFountCycle().longValue())
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
                    TaskStatistics taskStatistics = new TaskStatistics();
                    taskStatistics.setCyclePerTime(new LongAccumulator(function, t.getValue().getCyclePerTime()));
                    taskStatistics.setResourceFountCycle(new LongAccumulator(function, t.getValue().getResourceFountCycle()));
                    taskStatistics.setResourceFountCycle(new LongAccumulator(function, t.getValue().getResourceNotFoundCycle()));
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
    protected ComponentStatus rpcSend(ComponentStatus rpcReq) {
        return stub.rpcComponentStatus(rpcReq);
    }

    public void setStub(RpcComponentStatusGrpc.RpcComponentStatusBlockingStub stub) {
        this.stub = stub;
    }

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }
}
