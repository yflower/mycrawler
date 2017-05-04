package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.download.TaskTag;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/5/4.
 */
@Component
public class DownloadTaskConfigServer extends RpcDownloadTaskGrpc.RpcDownloadTaskImplBase {

    private static final Logger logger = LoggerFactory.getLogger(DownloadTaskConfigServer.class);

    @Autowired
    private DownLoadContext downLoadContext;

    @Override
    public void downloadTaskConfig(TaskTag request, StreamObserver<DownloadTask> responseObserver) {
        ComponentTaskService componentTaskService = new ComponentTaskService(downLoadContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
        responseObserver.onCompleted();
    }

    private class ComponentTaskService extends AbstractComponentTaskConfigServer<DownLoadContext, TaskTag, DownloadTask> {

        public ComponentTaskService(DownLoadContext downLoadContext) {
            this.componentContext = downLoadContext;
        }

        @Override
        protected <T extends AbstractTask> T internalTaskConfig(String taskTag) {
            AbstractTask abstractTask = componentContext.componentStatus().getTasks()
                    .stream().filter(t -> t.getTaskTag().equals(taskTag)).findFirst()
                    .get();
            return (T) abstractTask;
        }

        @Override
        protected String rpcResToLocal(TaskTag rpcRes) {
            return rpcRes.getTaskTag();
        }

        @Override
        protected <T extends AbstractTask> DownloadTask localToRPC_Q(T config) {
            Task task = (Task) config;
            return DownloadTask.newBuilder()
                    .setTaskTag(task.getTaskTag())
                    .setDynamic(task.isDynamic())
                    .setTest(task.isTest())
                    .addAllStartUrl(task.getStartUrls())
                    .addAllPre(
                            task.getPres().stream()
                                    .map(t -> DownloadTask.Processor.newBuilder()
                                            .setOrder(t.getOrder())
                                            .setType(this.downloadOperationTypeToRPCDownloadProcessType(t.getType()))
                                            .setQuery(t.getQuery())
                                            .setValue(t.getValue() == null ? "" : t.getValue())
                                            .build()
                                    ).collect(Collectors.toList())
                    )
                    .addAllPost(
                            task.getPosts().stream()
                                    .map(t -> DownloadTask.Processor.newBuilder()
                                            .setOrder(t.getOrder())
                                            .setType(this.downloadOperationTypeToRPCDownloadProcessType(t.getType()))
                                            .setQuery(t.getQuery())
                                            .setValue(t.getValue() == null ? "" : t.getValue())
                                            .build()
                                    ).collect(Collectors.toList())
                    )
                    .build();
        }

        private DownloadTask.Processor.Type downloadOperationTypeToRPCDownloadProcessType(Task.process.type type) {
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
                default:
                    return null;
            }
        }

    }


}
