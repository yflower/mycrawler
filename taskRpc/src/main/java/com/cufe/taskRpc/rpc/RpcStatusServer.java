package com.cufe.taskRpc.rpc;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.server.AbstractComponentStatusServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.TaskStatistics;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.ComponentType;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.proto.status.Status;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
public class RpcStatusServer extends RpcComponentStatusGrpc.RpcComponentStatusImplBase {

    public RpcStatusServer(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    private ComponentContext componentContext;


    @Override
    public void rpcComponentStatus(ComponentStatus request, StreamObserver<ComponentStatus> responseObserver) {
        RpcComponentStatusService rpcComponentStatusService = new RpcComponentStatusService(componentContext);
        ComponentStatus componentStatus = rpcComponentStatusService.componentStatus(request);
        responseObserver.onNext(componentStatus);
        responseObserver.onCompleted();
    }


    private class RpcComponentStatusService extends AbstractComponentStatusServer<ComponentStatus, ComponentStatus> {
        public RpcComponentStatusService(ComponentContext componentContext) {
            this.componentContext = componentContext;
        }


        @Override
        protected com.cufe.taskProcessor.component.status.ComponentStatus rpcResToLocal(ComponentStatus rpcRes) {
            com.cufe.taskProcessor.component.status.ComponentStatus componentStatus = new com.cufe.taskProcessor.component.status.ComponentStatus();

            componentStatus.setComponentStatus(StatusEnum.numberOf(rpcRes.getComponentStatusValue()));
            componentStatus.setHost(rpcRes.getHost());
            componentStatus.setPort(rpcRes.getPort());
            componentStatus.setComponentType(rpcRes.getComponentTypeValue());
            componentStatus.setTasks(
                    rpcRes.getTasksMap().entrySet()
                            .stream().map(t -> {
                        LongBinaryOperator function = (l, r) -> l + r;
                        TaskStatistics taskStatistics = new TaskStatistics();
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
