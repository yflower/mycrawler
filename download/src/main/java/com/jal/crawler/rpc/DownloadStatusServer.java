package com.jal.crawler.rpc;

import com.cufe.taskProcessor.component.status.*;
import com.cufe.taskProcessor.rpc.server.AbstractComponentStatusServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.status.*;
import com.jal.crawler.proto.status.ComponentStatus;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
@Component
public class DownloadStatusServer extends RpcComponentStatusGrpc.RpcComponentStatusImplBase {
    @Autowired
    private DownLoadContext downLoadContext;

    @Override
    public void rpcComponentStatus(ComponentStatus request, StreamObserver<ComponentStatus> responseObserver) {
        ComponentStatusService componentStatusService = new ComponentStatusService(downLoadContext);

        responseObserver.onNext(componentStatusService.componentStatus(request));
        responseObserver.onCompleted();
    }


    private class ComponentStatusService extends AbstractComponentStatusServer<DownLoadContext, ComponentStatus, ComponentStatus> {
        public ComponentStatusService(DownLoadContext context) {
            super.componentContext = context;
        }

        @Override
        public int componentType() {
            return ComponentType.DOWNLOAD_VALUE;
        }

        @Override
        protected com.cufe.taskProcessor.component.status.ComponentStatus rpcResToLocal(ComponentStatus rpcRes) {
            return null;
        }

        @Override
        protected ComponentStatus localToRPC_Q(com.cufe.taskProcessor.component.status.ComponentStatus result) {
            com.cufe.taskProcessor.component.status.ComponentStatus componentStatus =  result;
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

