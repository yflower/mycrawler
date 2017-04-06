package com.jal.crawler.proto;

import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.service.AbstractComponentStatusService;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.status.*;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
@Component
public class ComponentStatusServer extends RpcComponentStatusGrpc.RpcComponentStatusImplBase {
    @Autowired
    private DownLoadContext downLoadContext;


    @Override
    public void rpcComponentStatus(ConfigComponentStatus request, StreamObserver<ComponentStatus> responseObserver) {
        ComponentStatusService statusService = new ComponentStatusService();
        statusService.setComponentContext(downLoadContext);
        com.cufe.taskProcessor.model.ComponentStatus componentStatus = statusService.componentStatus(null);

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
                            .setEndTime(t.getTaskStatistics().getEndTime()==null?0:
                                    t.getTaskStatistics().getEndTime()
                                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                            .setResourceTotal((int) t.getTaskStatistics().getResourceTotalCycle())
                            .setProcessorTotal((int) t.getTaskStatistics().getProcessorTotalCycle())
                            .setPersistTotal((int) t.getTaskStatistics().getPersistTotalCycle())

                            .build())));
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    private class ComponentStatusService extends AbstractComponentStatusService {
        @Override
        public int componentType() {
            return ComponentType.DOWNLOAD_VALUE;
        }

    }

}

