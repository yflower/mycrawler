package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.resolve.TaskTag;

import java.util.HashMap;
import java.util.Map;

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
        result.put("vars", rpcRes.getVarList());
        result.put("items", rpcRes.getItemList());
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
