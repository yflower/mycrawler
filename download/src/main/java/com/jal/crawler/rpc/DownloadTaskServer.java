package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskServer;
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
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
        ComponentTaskService taskService = new ComponentTaskService(downLoadContext);


        responseObserver.onNext(taskService.task(request));
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


    private class ComponentTaskService extends AbstractComponentTaskServer<DownLoadContext, DownloadTask, DownloadTaskResponse> {
        public ComponentTaskService(DownLoadContext context) {
            super.componentContext = context;
        }


        @Override
        protected AbstractTask generateTask(String taskTag, Map taskOp) {
            Task task = new Task();
            task.setTaskTag((String) taskOp.get("taskTag"));
            task.setDynamic((Boolean) taskOp.get("dynamic"));
            task.setTest((Boolean) taskOp.get("test"));
            task.setStartUrls((Set<String>) taskOp.get("urls"));
            //目前只有动态的processor
            if (task.isDynamic()) {
                task.setPreProcessor(downLoad -> {
                    DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
                    ((List<DownloadTask.Processor>) taskOp.get("pre")).stream()
                            .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                            .forEach(pro -> processor(pro, dynamicDownload));
                });
                task.setPostProcessor(
                        downLoad -> {
                            DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
                            ((List<DownloadTask.Processor>) taskOp.get("pre")).stream()
                                    .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                                    .forEach(pro -> processor(pro, dynamicDownload));
                        });

            } else {
                //静态处理器
            }
            return task;
        }


        @Override
        protected Map<String, Object> rpcResToLocal(DownloadTask rpcRes) {
            Map<String, Object> ops = new HashMap();
            ops.put("taskType", rpcRes.getTaskType().getNumber());
            ops.put("taskTag", rpcRes.getTaskTag());
            ops.put("test", rpcRes.getTest());
            ops.put("dynamic", rpcRes.getDynamic());
            ops.put("urls", rpcRes.getStartUrlList());
            ops.put("pre", rpcRes.getPreList());
            ops.put("post", rpcRes.getPostList());
            return ops;
        }

        @Override
        protected DownloadTaskResponse localToRPC_Q(boolean result) {
            return DownloadTaskResponse.newBuilder().setOpStatus(result ? OPStatus.SUCCEED : OPStatus.FAILD).build();
        }
    }
}
