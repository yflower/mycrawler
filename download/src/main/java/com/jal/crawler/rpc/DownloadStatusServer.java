package com.jal.crawler.rpc;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.server.AbstractComponentStatusServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.status.*;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
@Component
public class DownloadStatusServer extends RpcComponentStatusGrpc.RpcComponentStatusImplBase {
    @Autowired
    private ComponentContext componentContext;

    @Override
    public void rpcComponentStatus(ComponentStatus request, StreamObserver<ComponentStatus> responseObserver) {
        ComponentStatusService componentStatusService = new ComponentStatusService(componentContext);

        responseObserver.onNext(componentStatusService.componentStatus(request));
        responseObserver.onCompleted();
    }


    private class ComponentStatusService extends AbstractComponentStatusServer< ComponentStatus, ComponentStatus> {
        public ComponentStatusService(ComponentContext context) {
            super.componentContext = context;
        }

        @Override
        public int componentType() {
            return ComponentType.DOWNLOAD_VALUE;
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
                        com.cufe.taskProcessor.task.TaskStatistics taskStatistics = new com.cufe.taskProcessor.task.TaskStatistics();
                        taskStatistics.setResourceTotalCycle(t.getValue().getResourceTotal());
                        taskStatistics.setProcessorTotalCycle(t.getValue().getProcessorTotal());
                        taskStatistics.setPersistTotalCycle(t.getValue().getPersistTotal());
                        taskStatistics.setBeginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getStartTime()), ZoneOffset.systemDefault()));
                        taskStatistics.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getEndTime()), ZoneOffset.systemDefault()));
                        AbstractTask task = new Task();
                        task.setStatus(StatusEnum.numberOf(t.getValue().getStatusValue()));
                        task.setTaskStatistics(taskStatistics);
                        task.setTaskTag(t.getKey());
                        task.setTest(t.getValue().getTest());
                        return task;
                    }).collect(Collectors.toList()));

            return componentStatus;
        }

        @Override
        protected ComponentStatus localToRPC_Q(com.cufe.taskProcessor.component.status.ComponentStatus result) {
            com.cufe.taskProcessor.component.status.ComponentStatus componentStatus = result;
            ComponentStatus.Builder builder = ComponentStatus.newBuilder();
            builder.setComponentStatus(Status.forNumber(componentStatus.getComponentStatus().getCode()));
            builder.setHost(componentStatus.getHost());
            builder.setPort(componentStatus.getPort());
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
                                .setTest(t.isTest())

                                .build())));
            }
            return builder.build();
        }


    }

}

