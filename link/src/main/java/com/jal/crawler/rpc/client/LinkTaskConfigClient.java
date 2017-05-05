package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskConfigClient;
import com.jal.crawler.proto.link.LinkTask;
import com.jal.crawler.proto.link.RpcLinkTaskGrpc;
import com.jal.crawler.proto.link.TaskTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianganlan on 2017/5/4.
 */
public class LinkTaskConfigClient extends AbstractComponentTaskConfigClient<LinkTask, TaskTag> {
    private RpcLinkTaskGrpc.RpcLinkTaskBlockingStub stub;


    @Override
    protected TaskTag localReqToRpcReq(String taskTag) {
        return TaskTag.newBuilder().setTaskTag(taskTag).build();
    }

    @Override
    protected Map<String, Object> rpcResToLocalRes(LinkTask rpcRes) {
        Map<String, Object> result = new HashMap();
        result.put("linkPattern", rpcRes.getLinkPatternList());
        return result;
    }

    @Override
    protected LinkTask rpcSend(TaskTag rpcReq) {
        return stub.linkTaskConfig(rpcReq);
    }


    public void setStub(RpcLinkTaskGrpc.RpcLinkTaskBlockingStub stub) {
        this.stub = stub;
    }
}
