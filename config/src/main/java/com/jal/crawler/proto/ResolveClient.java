package com.jal.crawler.proto;

import com.google.common.util.concurrent.ListenableFuture;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.resolve.*;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.proto.task.TaskType;
import io.grpc.ManagedChannel;

import java.util.concurrent.ExecutionException;

/**
 * Created by jal on 2017/1/23.
 */
public class ResolveClient extends AbstractComponentClient<ResolveConfig, ResolveTask> {


    @Override
    protected OPStatus internalTask(ResolveTask taskOperation, ManagedChannel channel) throws InterruptedException, ExecutionException {
        return result(taskOperation, channel);
    }

    @Override
    protected ResolveConfig internalConfigShow(ManagedChannel channel) {
        RpcResolveConfigGrpc.RpcResolveConfigBlockingStub blockingStub = RpcResolveConfigGrpc.newBlockingStub(channel);
        return blockingStub.resolveConfigShow(ConfigComponentStatus.getDefaultInstance());
    }

    @Override
    protected boolean internalConfigSet(ResolveConfig config) {
        RpcResolveConfigGrpc.RpcResolveConfigBlockingStub blockingStub = RpcResolveConfigGrpc.newBlockingStub(channel);
        ConfigStatus status = blockingStub.resolveConfig(config);
        return status.getInit();
    }

    @Override
    protected boolean validConfig(ResolveConfig config) {
        return true;
    }

    @Override
    protected TaskType taskType(ResolveTask task) {
        return task.getTaskType();
    }

    @Override
    protected String taskTag(ResolveTask task) {
        return task.getTaskTag();
    }

    private OPStatus result(ResolveTask task, ManagedChannel channel) throws InterruptedException, ExecutionException {
        RpcResolveTaskGrpc.RpcResolveTaskFutureStub futureStub = RpcResolveTaskGrpc.newFutureStub(channel);
        ListenableFuture<ResolveTaskResponse> future = futureStub.resolveTask(task);
        ResolveTaskResponse resolveTaskResponse = ResolveTaskResponse.getDefaultInstance();
        try {
            resolveTaskResponse = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw e;
        }
        return resolveTaskResponse.getOpStatus();
    }
}
