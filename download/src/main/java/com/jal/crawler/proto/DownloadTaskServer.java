package com.jal.crawler.proto;

import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.download.DownloadProcessor;
import com.jal.crawler.download.DynamicDownload;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.DownloadTaskResponse;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;


/**
 * Created by jal on 2017/1/25.
 */
@Component
public class DownloadTaskServer extends RpcDownloadTaskGrpc.RpcDownloadTaskImplBase {

    private static final Logger logger = LoggerFactory.getLogger(DownloadConfigServer.class);

    @Autowired
    private DownLoadContext downLoadContext;

    @Override
    public void downloadTask(DownloadTask request, StreamObserver<DownloadTaskResponse> responseObserver) {
        DownloadTaskResponse.Builder responseBuilder = DownloadTaskResponse.newBuilder();
        if (isRun(downLoadContext)) {
            if (request.getTaskType() == TaskType.ADD) {
                Task task = new Task();
                task.setTaskTag(request.getTaskTag());
                task.setDynamic(request.getDynamic());
                task.setStartUrls(new HashSet<>(request.getStartUrlList()));
                //目前只有动态的processor
                if (request.getDynamic()) {
                    DownloadProcessor pre = downLoad -> {
                        DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
                        request.getPreList().stream()
                                .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                                .forEach(pro -> processor(pro, dynamicDownload));
                    };
                    DownloadProcessor post = downLoad -> {
                        DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
                        request.getPostList().stream()
                                .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                                .forEach(pro -> processor(pro, dynamicDownload));
                    };
                    task.setPreProcessor(pre);
                    task.setPostProcessor(post);
                } else {
                    //静态处理器
                }
                task.setStatus(1);
                downLoadContext.addTask(task);
                logger.info("success to add download task {}", task.getTaskTag());
                responseBuilder.setTaskTag(task.getTaskTag());
                responseBuilder.setOpStatus(OPStatus.SUCCEED);
            } else if (request.getTaskType() == TaskType.STOP) {
                //停止download
                boolean is = downLoadContext.stopTask(request.getTaskTag());
                logger.info("success to stop download task {}", request.getTaskTag());
                responseBuilder.setTaskTag(request.getTaskTag());
                responseBuilder.setOpStatus(is ? OPStatus.SUCCEED : OPStatus.FAILD);
            } else if (request.getTaskType() == TaskType.FINISH) {
                //完成任务指令
                boolean is = downLoadContext.finishTask(request.getTaskTag());
                logger.info("success to finish download task {}", request.getTaskTag());
                responseBuilder.setTaskTag(request.getTaskTag());
                responseBuilder.setOpStatus(is ? OPStatus.SUCCEED : OPStatus.FAILD);
            } else {
                //完成任务指令
                boolean is = downLoadContext.destroyTask(request.getTaskTag());
                logger.info("success to destroy download task {}", request.getTaskTag());
                responseBuilder.setTaskTag(request.getTaskTag());
                responseBuilder.setOpStatus(is ? OPStatus.SUCCEED : OPStatus.FAILD);
            }
        } else {
            //组件还未启动
            responseBuilder.setOpStatus(OPStatus.FAILD);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private void processor(DownloadTask.Processor action, DynamicDownload dynamicDownload) {
        switch (action.getType()) {
            case CLICK:
                dynamicDownload.click(action.getQuery());
                break;
            case INPUT:
                dynamicDownload.input(action.getQuery(), action.getValue());
                break;
            case INPUT_SUBMIT:
                dynamicDownload.inputSubmit(action.getQuery());
                break;
            case LINK_TO:
                dynamicDownload.linkTo(action.getQuery());
                break;
            case WAIT_UTIL:
                dynamicDownload.waitUtilShow(action.getQuery(), 2, TimeUnit.SECONDS);
                break;
            case UNRECOGNIZED:
                break;
        }
    }

    private boolean isRun(DownLoadContext context) {
        return context.status() == 2;
    }
}
