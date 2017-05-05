package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.LinkContext;
import com.jal.crawler.proto.link.LinkTask;
import com.jal.crawler.proto.link.LinkTaskResponse;
import com.jal.crawler.proto.link.RpcLinkTaskGrpc;
import com.jal.crawler.proto.link.TaskTag;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by jal on 2017/1/23.
 */
@Component
public class LinkTaskServer extends RpcLinkTaskGrpc.RpcLinkTaskImplBase {
    private static final Logger logger = LoggerFactory.getLogger(LinkTaskServer.class);

    @Autowired
    private LinkContext linkContext;

    @Override
    public void linkTask(LinkTask request, StreamObserver<LinkTaskResponse> responseObserver) {
        ComponentTaskService taskService = new ComponentTaskService(linkContext);
        responseObserver.onNext(taskService.task(request));
        responseObserver.onCompleted();
    }

    @Override
    public void linkTaskConfig(TaskTag request, StreamObserver<LinkTask> responseObserver) {
        ComponentTaskConfigService componentTaskService = new ComponentTaskConfigService(linkContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
        responseObserver.onCompleted();
    }


    private class ComponentTaskService extends AbstractComponentTaskServer<LinkContext, LinkTask, LinkTaskResponse> {


        public ComponentTaskService(LinkContext linkContext) {
            this.componentContext = linkContext;
        }

        @Override
        protected AbstractTask generateTask(String taskTag, Map<String, Object> taskOp) {
            Task task = new Task();
            task.setTaskTag(taskTag);
            task.setTest((Boolean) taskOp.get("test"));
            task.setLinkPattern((List<String>) taskOp.get("linkPattern"));
            return task;
        }

        @Override
        protected Map<String, Object> rpcResToLocal(LinkTask rpcRes) {
            Map<String, Object> ops = new HashMap();
            ops.put("taskType", rpcRes.getTaskType().getNumber());
            ops.put("taskTag", rpcRes.getTaskTag());
            ops.put("test", rpcRes.getTest());
            ops.put("linkPattern", rpcRes.getLinkPatternList());
            return ops;
        }

        @Override
        protected LinkTaskResponse localToRPC_Q(boolean result) {
            return LinkTaskResponse.newBuilder()
                    .setOpStatus(result ? OPStatus.SUCCEED : OPStatus.FAILD)
                    .build();

        }
    }

    private class ComponentTaskConfigService extends AbstractComponentTaskConfigServer<LinkContext, TaskTag, LinkTask> {

        public ComponentTaskConfigService(LinkContext linkContext) {
            this.componentContext = linkContext;
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
        protected <T extends AbstractTask> LinkTask localToRPC_Q(T config) {
            Task resolveTask = (Task) config;
            if (resolveTask.getLinkPattern() == null) {
                resolveTask.setLinkPattern(new ArrayList<>());
            }
            return LinkTask.newBuilder()
                    .setTaskTag(resolveTask.getTaskTag())
                    .setTest(resolveTask.isTest())
                    .addAllLinkPattern(resolveTask.getLinkPattern())
                    .build();
        }


    }
}
