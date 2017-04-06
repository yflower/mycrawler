package com.jal.crawler.rpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.download.*;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.proto.task.TaskType;
import io.grpc.ManagedChannel;


import java.util.concurrent.ExecutionException;

/**
 * Created by jal on 2017/1/24.
 */
public class DownloadClient extends AbstractComponentClient<DownloadConfig, DownloadTask> {


    @Override
    protected OPStatus internalTask(DownloadTask taskOperation, ManagedChannel channel) throws InterruptedException, ExecutionException {
        return result(taskOperation, channel);
    }

    @Override
    protected DownloadConfig internalConfigShow(ManagedChannel channel) {
        RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub blockingStub = RpcDownlandConfigGrpc.newBlockingStub(channel);
        return blockingStub.downloadConfigShow(ConfigComponentStatus.getDefaultInstance());
    }

    @Override
    protected boolean internalConfigSet(DownloadConfig config) {
        RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub blockingStub = RpcDownlandConfigGrpc.newBlockingStub(channel);
        ConfigStatus status = blockingStub.downloadConfig(config);
        return status.getInit();
    }

    @Override
    protected boolean validConfig(DownloadConfig config) {
        return true;
    }

    @Override
    protected TaskType taskType(DownloadTask task) {
        return task.getTaskType();
    }

    @Override
    protected String taskTag(DownloadTask task) {
        return task.getTaskTag();
    }


    private OPStatus result(DownloadTask task, ManagedChannel channel) throws InterruptedException, ExecutionException {
        RpcDownloadTaskGrpc.RpcDownloadTaskFutureStub futureStub = RpcDownloadTaskGrpc.newFutureStub(channel);
        ListenableFuture<DownloadTaskResponse> future = futureStub.downloadTask(task);
        DownloadTaskResponse downloadTaskResponse = DownloadTaskResponse.getDefaultInstance();
        try {
            downloadTaskResponse = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw e;
        }
        return downloadTaskResponse.getOpStatus();
    }


}
