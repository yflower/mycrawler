package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.TaskTypeEnum;
import com.jal.crawler.data.DataTypeEnum;
import com.jal.crawler.proto.data.DataTask;
import com.jal.crawler.proto.data.DataTaskResponse;
import com.jal.crawler.proto.data.RpcDataTaskGrpc;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import com.jal.crawler.web.param.rpc.DataTaskOpRpcParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class DataTaskClient extends AbstractComponentTaskClient<DataTaskResponse, DataTask> {
    private RpcDataTaskGrpc.RpcDataTaskBlockingStub stub;

    @Override
    protected <P> AbstractTask generateTask(String taskTag, P params) {
        DataTaskOpRpcParam rpcParam = (DataTaskOpRpcParam) params;
        Task task = new Task();
        task.setTest(rpcParam.isTest());
        task.setTaskTag(rpcParam.getTaskTag());
        task.setDataType(DataTypeEnum.numberOf(rpcParam.getDataType()));
        return task;
    }

    @Override
    protected DataTask localReqToRpcReq(AbstractTask task, String taskTag, TaskTypeEnum taskType) {
        Task dataTask = (Task) task;
        return DataTask.newBuilder()
                .setTaskTag(taskTag)
                .setTest(dataTask.isTest())
                .setTaskType(TaskType.forNumber(taskType.getCode()))
                .setDataTypeValue(dataTask.getDataType().getType())
                .build();
    }

    @Override
    protected boolean rpcResToLocalRes(DataTaskResponse rpcRes) {
        return rpcRes.isInitialized();
    }

    @Override
    protected DataTaskResponse rpcSend(DataTask rpcReq) {
        return stub.dataTask(rpcReq);
    }

    public void setStub(RpcDataTaskGrpc.RpcDataTaskBlockingStub stub) {
        this.stub = stub;
    }
}
