package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.TaskTypeEnum;
import com.jal.crawler.proto.link.LinkTask;
import com.jal.crawler.proto.link.LinkTaskResponse;
import com.jal.crawler.proto.link.RpcLinkTaskGrpc;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import com.jal.crawler.web.param.rpc.LinkTaskOpRpcParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class LinkTaskClient extends AbstractComponentTaskClient<LinkTaskResponse, LinkTask> {
    private RpcLinkTaskGrpc.RpcLinkTaskBlockingStub stub;

    @Override
    protected <P> AbstractTask generateTask(String taskTag, P params) {
        LinkTaskOpRpcParam rpcParam = (LinkTaskOpRpcParam) params;
        Task task = new Task();
        task.setTest(rpcParam.isTest());
        task.setTaskTag(rpcParam.getTaskTag());
        task.setLinkPattern(rpcParam.getLinkPattern());
        return task;
    }

    @Override
    protected LinkTask localReqToRpcReq(AbstractTask task, String taskTag, TaskTypeEnum taskType) {
        if (taskType == TaskTypeEnum.ADD) {
            Task linkTask = (Task) task;
            return LinkTask.newBuilder()
                    .setTaskTag(taskTag)
                    .setTest(linkTask.isTest())
                    .setTaskType(TaskType.forNumber(taskType.getCode()))
                    .addAllLinkPattern(linkTask.getLinkPattern())

                    .build();
        } else {
            return LinkTask.newBuilder()
                    .setTaskTag(taskTag)
                    .setTaskType(TaskType.forNumber(taskType.getCode()))
                    .build();
        }

    }

    @Override
    protected boolean rpcResToLocalRes(LinkTaskResponse rpcRes) {
        return rpcRes.isInitialized();
    }

    @Override
    protected LinkTaskResponse rpcSend(LinkTask rpcReq) {
        return stub.linkTask(rpcReq);
    }

    public void setStub(RpcLinkTaskGrpc.RpcLinkTaskBlockingStub stub) {
        this.stub = stub;
    }
}
