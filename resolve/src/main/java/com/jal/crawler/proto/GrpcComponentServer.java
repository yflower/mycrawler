package com.jal.crawler.proto;

import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
@Component
public class GrpcComponentServer extends RpcComponentStatusGrpc.RpcComponentStatusImplBase {
    @Autowired
    private ResolveContext resolveContext;

    @Override
    public void rpcComponentStatus(ConfigComponentStatus request, StreamObserver<ComponentStatus> responseObserver) {
        //处理config tag
        ComponentStatus.Builder builder = ComponentStatus.newBuilder();
        builder.setComponentStatus(ComponentStatus.Status.forNumber(resolveContext.status()));
        if (isRun(resolveContext)) {
            builder.setTaskNum(resolveContext.tasks().size());
            builder.putAllTasks(resolveContext.tasks().stream()
                    .collect(Collectors.toMap(t -> t.getTaskTag(), t -> ComponentStatus.Status.forNumber(t.getStatus()))));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    private boolean isRun(ResolveContext context) {
        return context.status() == 2;
    }
}
