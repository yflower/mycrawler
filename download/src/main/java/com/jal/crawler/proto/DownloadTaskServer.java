package com.jal.crawler.proto;

import com.cufe.taskProcessor.enums.TaskTypeEnum;
import com.cufe.taskProcessor.service.AbstractComponentTaskService;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.download.DownloadProcessor;
import com.jal.crawler.download.DynamicDownload;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.DownloadTaskResponse;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by jal on 2017/1/25.
 */
@Component
public class DownloadTaskServer extends RpcDownloadTaskGrpc.RpcDownloadTaskImplBase {

    private static final Logger logger = LoggerFactory.getLogger(DownloadInitServer.class);

    @Autowired
    private DownLoadContext downLoadContext;

    @Override
    public void downloadTask(DownloadTask request, StreamObserver<DownloadTaskResponse> responseObserver) {
        ComponentTaskService taskService = new ComponentTaskService();
        taskService.setComponentContext(downLoadContext);
        taskService.dynamic = request.getDynamic();
        taskService.test = request.getTest();
        taskService.urls = new HashSet<>(request.getStartUrlList());

        taskService.pre = downLoad -> {
            DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
            request.getPreList().stream()
                    .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                    .forEach(pro -> processor(pro, dynamicDownload));
        };
        taskService.post = downLoad -> {
            DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
            request.getPostList().stream()
                    .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                    .forEach(pro -> processor(pro, dynamicDownload));
        };
        boolean result = taskService.task(request.getTaskTag(), TaskTypeEnum.numberOf(request.getTaskTypeValue()));


        DownloadTaskResponse.Builder responseBuilder = DownloadTaskResponse.newBuilder();

        responseBuilder.setOpStatus(result ? OPStatus.SUCCEED : OPStatus.FAILD);
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


    private class ComponentTaskService extends AbstractComponentTaskService {
        boolean dynamic;
        boolean test;
        Set<String> urls;
        DownloadProcessor pre;
        DownloadProcessor post;

        @Override
        public AbstractTask generateTask(String taskTag) {
            Task task = new Task();
            task.setTaskTag(taskTag);
            task.setDynamic(dynamic);
            task.setTest(test);
            task.setStartUrls(urls);
            //目前只有动态的processor
            if (dynamic) {
                task.setPreProcessor(pre);
                task.setPostProcessor(post);
            } else {
                //静态处理器
            }
            return task;
        }
    }
}
