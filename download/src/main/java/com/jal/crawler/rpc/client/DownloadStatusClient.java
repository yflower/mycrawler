package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.rpc.client.AbstractComponentStatusClient;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.TaskStatistics;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.status.ComponentType;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.task.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadStatusClient extends AbstractComponentStatusClient<DownLoadContext, com.jal.crawler.proto.status.ComponentStatus, com.jal.crawler.proto.status.ComponentStatus> {
    private RpcComponentStatusGrpc.RpcComponentStatusBlockingStub stub;

    @Override
    protected int componentType() {
        return ComponentType.DOWNLOAD_VALUE;
    }

    @Override
    protected com.jal.crawler.proto.status.ComponentStatus localReqToRpcReq(ComponentStatus componentStatus) {
        return com.jal.crawler.proto.status.ComponentStatus.getDefaultInstance();
    }

    @Override
    protected ComponentStatus rpcResToLocalRes(com.jal.crawler.proto.status.ComponentStatus rpcRes) {

        ComponentStatus componentStatus = new ComponentStatus();
        componentStatus.setComponentStatus(StatusEnum.valueOf(rpcRes.getComponentStatus().name()));
        componentStatus.setTasks(
                rpcRes.getTasksMap().entrySet()
                        .stream().map(t -> {
                    //todo 数据没有取完整
                    TaskStatistics taskStatistics = new TaskStatistics();
                    taskStatistics.setResourceTotalCycle(t.getValue().getResourceTotal());
                    taskStatistics.setProcessorTotalCycle(t.getValue().getProcessorTotal());
                    taskStatistics.setPersistTotalCycle(t.getValue().getPersistTotal());
                    taskStatistics.setBeginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getStartTime()), ZoneOffset.systemDefault()));
                    taskStatistics.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getEndTime()), ZoneOffset.systemDefault()));
                    Task task = new Task();
                    task.setTaskTag(t.getKey());
                    task.setTaskStatistics(taskStatistics);
                    return task;
                }).collect(Collectors.toList()));

        return componentStatus;
    }

    @Override
    protected com.jal.crawler.proto.status.ComponentStatus rpcSend(com.jal.crawler.proto.status.ComponentStatus rpcReq) {
        return stub.rpcComponentStatus(rpcReq);
    }


    public void setStub(RpcComponentStatusGrpc.RpcComponentStatusBlockingStub stub) {
        this.stub = stub;
    }
}
