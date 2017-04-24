package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.TaskTypeEnum;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.ResolveTaskResponse;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import com.jal.crawler.web.param.rpc.ResolveTaskOpRpcParam;

import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveTaskClient extends AbstractComponentTaskClient<ResolveTaskResponse, ResolveTask> {
    private RpcResolveTaskGrpc.RpcResolveTaskBlockingStub stub;

    @Override
    protected <P> AbstractTask generateTask(String taskTag, P params) {
        ResolveTaskOpRpcParam rpcParam = (ResolveTaskOpRpcParam) params;
        Task task = new Task();
        task.setTest(rpcParam.isTest());
        task.setTaskTag(rpcParam.getTaskTag());
        task.setVars(rpcParam.getVars());
        task.setItems(rpcParam.getItems());
        return task;
    }

    @Override
    protected ResolveTask localReqToRpcReq(AbstractTask task, String taskTag, TaskTypeEnum taskType) {
        Task resolveTask = (Task) task;
        return ResolveTask.newBuilder()
                .setTaskTag(taskTag)
                .setTest(resolveTask.isTest())
                .setTaskType(TaskType.forNumber(taskType.getCode()))
                .addAllVar(
                        resolveTask.getVars().stream()
                                .map(t -> ResolveTask.Var.newBuilder()
                                        .setName(t.getName())
                                        .setQuery(t.getQuery())
                                        .setOption(t.getOption())
                                        .setOptionValue(t.getOptionValue())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .addAllItem(
                        resolveTask.getItems().stream()
                                .map(t -> ResolveTask.Item.newBuilder()
                                        .setItemName(t.getItemName())
                                        .addAllVar(
                                                t.getItemVar().stream()
                                                        .map(tx -> ResolveTask.Var.newBuilder()
                                                                .setName(tx.getName())
                                                                .setQuery(tx.getQuery())
                                                                .setOption(tx.getOptionValue())
                                                                .setOptionValue(tx.getOptionValue())
                                                                .build()
                                                        )
                                                        .collect(Collectors.toList())
                                        )
                                        .build()
                                )
                                .collect(Collectors.toList())

                )
                .build();
    }

    @Override
    protected boolean rpcResToLocalRes(ResolveTaskResponse rpcRes) {
        return rpcRes.isInitialized();
    }

    @Override
    protected ResolveTaskResponse rpcSend(ResolveTask rpcReq) {
        return stub.resolveTask(rpcReq);
    }

    public void setStub(RpcResolveTaskGrpc.RpcResolveTaskBlockingStub stub) {
        this.stub = stub;
    }
}
