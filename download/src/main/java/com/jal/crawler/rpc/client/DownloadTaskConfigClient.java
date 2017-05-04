package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.download.TaskTag;
import com.jal.crawler.proto.task.TaskType;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

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
        result.put("pre", rpcRes.getPreList());
        result.put("post", rpcRes.getPostList());
        result.put("urls", rpcRes.getStartUrlList());

        return result;
    }

    @Override
    protected DownloadTask rpcSend(TaskTag rpcReq) {
        return stub.downloadTaskConfig(rpcReq);
    }

    public void setStub(RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub stub) {
        this.stub = stub;
    }
}
