package com.jal.crawler.proto;

import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.ResolveTaskResponse;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by jal on 2017/1/23.
 */
@Component
public class GrpcTaskServer extends RpcResolveTaskGrpc.RpcResolveTaskImplBase {
    private static final Logger logger = LoggerFactory.getLogger(GrpcTaskServer.class);

    @Autowired
    private ResolveContext resolveContext;

    @Override
    public void resolveTask(ResolveTask request, StreamObserver<ResolveTaskResponse> responseObserver) {
        ResolveTaskResponse.Builder builder = ResolveTaskResponse.newBuilder();
        if (isRun(resolveContext)) {
            if (request.getTaskType() == TaskType.ADD) {
                Task task = new Task(request.getTaskTag());
                request.getVarList().stream().forEach(t -> task.var(t.getName(), t.getQuery(),t.getOption(),t.getOptionValue()));
                task.setStatus(1);
                resolveContext.addTask(task);
                logger.info("success to add resolve task {}", task.getTaskTag());
                builder.setOpStatus(OPStatus.SUCCEED);
                builder.setTaskTag(request.getTaskTag());
            } else if (request.getTaskType() == TaskType.STOP) {
                boolean is = resolveContext.stopTask(request.getTaskTag());
                logger.info("success to stop resolve task {}", request.getTaskTag());
                builder.setTaskTag(request.getTaskTag());
                builder.setOpStatus(is ? OPStatus.SUCCEED : OPStatus.FAILD);
            } else if (request.getTaskType() == TaskType.FINISH) {
                boolean is = resolveContext.finishTask(request.getTaskTag());
                logger.info("success to finish resolve task {}", request.getTaskTag());
                builder.setTaskTag(request.getTaskTag());
                builder.setOpStatus(is ? OPStatus.SUCCEED : OPStatus.FAILD);
            } else {
                boolean is = resolveContext.destroyTask(request.getTaskTag());
                logger.info("success to destroy resolve task {}", request.getTaskTag());
                builder.setTaskTag(request.getTaskTag());
                builder.setOpStatus(is ? OPStatus.SUCCEED : OPStatus.FAILD);
            }

        } else {
            builder.setOpStatus(OPStatus.FAILD);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private boolean isRun(ResolveContext context) {
        return context.status() == 2;
    }
}
