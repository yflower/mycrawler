package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.download.TaskTag;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/5/4.
 */
public class DownloadTaskConfigClient extends AbstractComponentTaskConfigClient<DownloadTask,TaskTag> {
    private RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub stub;


    @Override
    protected TaskTag localReqToRpcReq(String taskTag) {
        return TaskTag.newBuilder().setTaskTag(taskTag).build();
    }

    @Override
    protected Map<String, Object> rpcResToLocalRes(DownloadTask rpcRes) {
        Map<String,Object> result=new HashMap();

        result.put("dynamic", rpcRes.getDynamic());
        result.put("pre", rpcRes.getPreList().stream().map(this::rpcProcessToLocalProcess).collect(Collectors.toList()));
        result.put("post", rpcRes.getPostList().stream().map(this::rpcProcessToLocalProcess).collect(Collectors.toList()));
        result.put("urls", rpcRes.getStartUrlList());

        return result;
    }

    private Task.process rpcProcessToLocalProcess(DownloadTask.Processor processor) {
        Task.process process = new Task.process();
        process.setOrder(processor.getOrder());
        process.setType(Task.process.type.numberOf(processor.getType().getNumber()));
        process.setQuery(processor.getQuery());
        process.setValue(processor.getValue());
        return process;
    }

    @Override
    protected DownloadTask rpcSend(TaskTag rpcReq) {
        return stub.downloadTaskConfig(rpcReq);
    }

    public void setStub(RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub stub) {
        this.stub = stub;
    }
}
