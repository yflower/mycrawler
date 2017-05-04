package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.resolve.TaskTag;
import com.jal.crawler.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/5/4.
 */
public class ResolveTaskConfigClient extends AbstractComponentTaskConfigClient<ResolveTask, TaskTag> {
    private RpcResolveTaskGrpc.RpcResolveTaskBlockingStub stub;


    @Override
    protected TaskTag localReqToRpcReq(String taskTag) {
        return TaskTag.newBuilder().setTaskTag(taskTag).build();
    }

    @Override
    protected Map<String, Object> rpcResToLocalRes(ResolveTask rpcRes) {
        Map<String, Object> result = new HashMap();
        List<Task.var> vars = rpcRes.getVarList().stream()
                .map(t -> new Task.var(t.getName(), t.getQuery(), t.getOption(), t.getOptionValue()))
                .collect(Collectors.toList());
        List<Task.item> items = rpcRes.getItemList().stream()
                .map(k -> {
                    Task.item item = new Task.item();
                    item.setItemName(k.getItemName());
                    item.setItemVar(k.getVarList().stream()
                            .map(t -> new Task.var(t.getName(), t.getQuery(), t.getOption(), t.getOptionValue()))
                            .collect(Collectors.toList()));
                    return item;
                }).collect(Collectors.toList());


        result.put("vars", vars);
        result.put("items", items);
        return result;
    }

    @Override
    protected ResolveTask rpcSend(TaskTag rpcReq) {
        return stub.resolveTaskConfig(rpcReq);
    }


    public void setStub(RpcResolveTaskGrpc.RpcResolveTaskBlockingStub stub) {
        this.stub = stub;
    }
}
