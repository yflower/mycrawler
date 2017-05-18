package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.download.DynamicDownload;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.download.DownloadTaskResponse;
import com.jal.crawler.proto.download.RpcDownloadTaskGrpc;
import com.jal.crawler.proto.download.TaskTag;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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

    @Override
    public void downloadTaskConfig(TaskTag request, StreamObserver<DownloadTask> responseObserver) {
        ComponentTaskConfigService componentTaskService = new ComponentTaskConfigService(downLoadContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
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
            case DOWN:
                dynamicDownload.download();
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
                task.setPres(((List<DownloadTask.Processor>) taskOp.get("pre"))
                        .stream().map(this::rpcProcessToLocalProcess).collect(Collectors.toList()));
                task.setPostProcessor(
                        downLoad -> {
                            DynamicDownload dynamicDownload = (DynamicDownload) downLoad;
                            List<DownloadTask.Processor> post = ((List<DownloadTask.Processor>) taskOp.get("post")).stream()
                                    .sorted(Comparator.comparingInt(DownloadTask.Processor::getOrder))
                                    .collect(Collectors.toList());
                            downLoad.setSkip(false);
                            downLoad.setPages(new ArrayList<>());
                            for(int i=0;i<post.size();++i){
                                if(downLoad.isSkip()){
                                    break;
                                }
                                if(post.get(i).getType()==DownloadTask.Processor.Type.GOTO){
                                    i=Integer.parseInt(post.get(i).getQuery());
                                }else {
                                    processor(post.get(i),dynamicDownload);
                                }
                            }

                        });
                task.setPosts(((List<DownloadTask.Processor>) taskOp.get("post"))
                        .stream()
                        .map(this::rpcProcessToLocalProcess).collect(Collectors.toList()));

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
            ops.put("urls", new HashSet<>(rpcRes.getStartUrlList()));
            ops.put("pre", new ArrayList<>(rpcRes.getPreList()));
            ops.put("post", new ArrayList<>(rpcRes.getPostList()));
            return ops;
        }

        @Override
        protected DownloadTaskResponse localToRPC_Q(boolean result) {
            return DownloadTaskResponse.newBuilder().setOpStatus(result ? OPStatus.SUCCEED : OPStatus.FAILD).build();
        }

        private Task.process rpcProcessToLocalProcess(DownloadTask.Processor processor) {
            Task.process process = new Task.process();
            process.setOrder(processor.getOrder());
            process.setType(Task.process.type.numberOf(processor.getType().getNumber()));
            process.setQuery(processor.getQuery());
            process.setValue(processor.getValue());
            return process;
        }
    }

    private class ComponentTaskConfigService extends AbstractComponentTaskConfigServer<DownLoadContext, TaskTag, DownloadTask> {

        public ComponentTaskConfigService(DownLoadContext downLoadContext) {
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
            if (task.getPres() == null) {
                task.setPres(new ArrayList<>());
            }
            if (task.getPosts() == null) {
                task.setPosts(new ArrayList<>());
            }
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
                case GOTO:
                    return DownloadTask.Processor.Type.GOTO;
                case DOWN:
                    return DownloadTask.Processor.Type.DOWN;
                default:
                    throw new IllegalStateException("有新的操作加入，但是为定义转化");
            }
        }


    }
}
