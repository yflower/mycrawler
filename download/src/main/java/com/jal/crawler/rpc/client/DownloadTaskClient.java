package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentTaskClient;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.TaskTypeEnum;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.DownloadTaskResponse;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import com.jal.crawler.web.param.rpc.DownloadRpcTaskOpParam;

import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadTaskClient extends AbstractComponentTaskClient<DownloadTaskResponse, DownloadTask> {

    private RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub stub;


    @Override
    protected <P> AbstractTask generateTask(String taskTag, P params) {
        DownloadRpcTaskOpParam opParam = (DownloadRpcTaskOpParam) params;
        Task task = new Task();
        task.setDynamic(opParam.isDynamic());
        task.setTest(opParam.isTest());
        task.setTaskTag(opParam.getTaskTag());
        task.setStartUrls(opParam.getUrls());
        task.setPres(opParam.getPreProcess());
        task.setPosts(opParam.getPostProcess());
        return task;
    }

    @Override
    protected DownloadTask localReqToRpcReq(AbstractTask task, String taskTag, TaskTypeEnum taskType) {
        if(taskType==TaskTypeEnum.ADD){
            Task downloadTask = (Task) task;
            return DownloadTask.newBuilder()
                    .setTaskType(TaskType.forNumber(taskType.getCode()))
                    .setTaskTag(taskTag)
                    .setDynamic(downloadTask.isDynamic())
                    .setTest(downloadTask.isTest())
                    .addAllStartUrl(downloadTask.getStartUrls())
                    .addAllPre(
                            downloadTask.getPres().stream()
                                    .map(t -> DownloadTask.Processor.newBuilder()
                                            .setOrder(t.getOrder())
                                            .setType(this.downloadOperationTypeToRPCDownloadProcessType(t.getType()))
                                            .setQuery(t.getQuery()==null?"":t.getQuery())
                                            .setValue(t.getValue()==null?"":t.getValue())
                                            .build()
                                    ).collect(Collectors.toList())
                    )
                    .addAllPost(
                            downloadTask.getPosts().stream()
                                    .map(t -> DownloadTask.Processor.newBuilder()
                                            .setOrder(t.getOrder())
                                            .setType(this.downloadOperationTypeToRPCDownloadProcessType(t.getType()))
                                            .setQuery(t.getQuery()==null?"":t.getQuery())
                                            .setValue(t.getValue()==null?"":t.getValue())
                                            .build()
                                    ).collect(Collectors.toList())
                    )
                    .build();
        }else {
            return DownloadTask.newBuilder()
                    .setTaskTag(taskTag)
                    .setTaskType(TaskType.forNumber(taskType.getCode()))
                    .build();
        }

    }

    @Override
    protected boolean rpcResToLocalRes(DownloadTaskResponse rpcRes) {
        return rpcRes.isInitialized();
    }

    @Override
    protected DownloadTaskResponse rpcSend(DownloadTask rpcReq) {
        return stub.downloadTask(rpcReq);
    }


    public void setStub(RpcDownloadTaskGrpc.RpcDownloadTaskBlockingStub stub) {
        this.stub = stub;
    }


    private static DownloadTask.Processor.Type downloadOperationTypeToRPCDownloadProcessType(Task.process.type type) {
        switch (type) {
            case CLICK:
                return DownloadTask.Processor.Type.CLICK;
            case INPUT:
                return DownloadTask.Processor.Type.INPUT;
            case INPUT_SUBMIT:
                return DownloadTask.Processor.Type.INPUT_SUBMIT;
            case LINK_TO:
                return DownloadTask.Processor.Type.LINK_TO;
            case WAIT_UTIL:
                return DownloadTask.Processor.Type.WAIT_UTIL;
            case GOTO:
                return DownloadTask.Processor.Type.GOTO;
            case DOWN:
                return DownloadTask.Processor.Type.DOWN;
            default:
                throw new IllegalStateException("有新的操作加入，但是为定义转化");
        }
    }
}
