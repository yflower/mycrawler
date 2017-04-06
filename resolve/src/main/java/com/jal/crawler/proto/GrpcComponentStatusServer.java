package com.jal.crawler.proto;

import com.cufe.taskProcessor.enums.StatusEnum;
import com.cufe.taskProcessor.service.AbstractComponentStatusService;
import com.jal.crawler.context.ResolveContext;
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
public class GrpcComponentStatusServer extends RpcComponentStatusGrpc.RpcComponentStatusImplBase {
    @Autowired
    private ResolveContext resolveContext;

    @Override
    public void rpcComponentStatus(ConfigComponentStatus request, StreamObserver<ComponentStatus> responseObserver) {
        ComponentStatusService componentStatusService = new ComponentStatusService();
        componentStatusService.setComponentContext(resolveContext);

        com.cufe.taskProcessor.model.ComponentStatus status = componentStatusService.componentStatus(null);

        //处理config tag
        ComponentStatus.Builder builder = ComponentStatus.newBuilder();
        builder.setComponentStatus(Status.forNumber(status.getComponentStatus().getCode()));
        builder.setComponentType(ComponentType.forNumber(status.getComponentType()));
        if (isRun(resolveContext)) {
            builder.setTaskNum(status.getTasks().size());
            builder.putAllTasks(status.getTasks().stream()
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
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();


    }

    private boolean isRun(ResolveContext context) {
        return context.getStatus() == StatusEnum.STARTED;
    }

    private class ComponentStatusService extends AbstractComponentStatusService {
        @Override
        public int componentType() {
            return ComponentType.RESOLVE_VALUE;
        }
    }
}
