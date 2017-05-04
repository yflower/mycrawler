package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.jal.crawler.proto.data.DataTask;
import com.jal.crawler.proto.data.RpcDataTaskGrpc;
import com.jal.crawler.proto.data.TaskTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianganlan on 2017/5/4.
 */
public class DataTaskConfigClient extends AbstractComponentTaskConfigClient<DataTask, TaskTag> {
    private RpcDataTaskGrpc.RpcDataTaskBlockingStub stub;


    @Override
    protected TaskTag localReqToRpcReq(String taskTag) {
        return TaskTag.newBuilder().setTaskTag(taskTag).build();
    }

    @Override
    protected Map<String, Object> rpcResToLocalRes(DataTask rpcRes) {
        Map<String, Object> result = new HashMap();
        result.put("dataType", rpcRes.getDataType().getNumber());
        return result;
    }

    @Override
    protected DataTask rpcSend(TaskTag rpcReq) {
        return stub.dataTaskConfig(rpcReq);
    }


    public void setStub(RpcDataTaskGrpc.RpcDataTaskBlockingStub stub) {
        this.stub = stub;
    }
}
