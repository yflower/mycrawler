package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.ResolveTaskResponse;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.resolve.TaskTag;
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
public class ResolveTaskServer extends RpcResolveTaskGrpc.RpcResolveTaskImplBase {
    private static final Logger logger = LoggerFactory.getLogger(ResolveTaskServer.class);

    @Autowired
    private ResolveContext resolveContext;

    @Override
    public void resolveTask(ResolveTask request, StreamObserver<ResolveTaskResponse> responseObserver) {
        ComponentTaskService taskService = new ComponentTaskService(resolveContext);
        responseObserver.onNext(taskService.task(request));
        responseObserver.onCompleted();
    }

    @Override
    public void resolveTaskConfig(TaskTag request, StreamObserver<ResolveTask> responseObserver) {
        ComponentTaskConfigService componentTaskService = new ComponentTaskConfigService(resolveContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
        responseObserver.onCompleted();
    }


    private class ComponentTaskService extends AbstractComponentTaskServer<ResolveContext, ResolveTask, ResolveTaskResponse> {


        public ComponentTaskService(ResolveContext resolveContext) {
            this.componentContext = resolveContext;
        }

        @Override
        protected AbstractTask generateTask(String taskTag, Map<String, Object> taskOp) {
            Task task = new Task();
            task.setTaskTag(taskTag);
            task.setTest((Boolean) taskOp.get("test"));
            List<Task.item> items = new ArrayList<>();

            ((List<com.jal.crawler.proto.resolve.ResolveTask.Item>)
                    taskOp.get("items")).stream().forEach(t -> {
                Task.item item = new Task.item(t.getItemName(),
                        t.getVarList().stream().map(
                                var -> new Task.var(var.getName(),
                                        var.getQuery(), var.getOption(),
                                        var.getOptionValue()))
                                .collect(Collectors.toList()));
                items.add(item);
            });

            List<Task.var> vars = new ArrayList<>();
            ((List<com.jal.crawler.proto.resolve.ResolveTask.Var>) taskOp.get("vars")).stream().forEach(t -> vars.add(new Task.var(t.getName(), t.getQuery(), t.getOption(), t.getOptionValue())));


            task.setVars(vars);
            task.setItems(items);
            return task;
        }

        @Override
        protected Map<String, Object> rpcResToLocal(ResolveTask rpcRes) {
            Map<String, Object> ops = new HashMap();
            ops.put("taskType", rpcRes.getTaskType().getNumber());
            ops.put("taskTag", rpcRes.getTaskTag());
            ops.put("test", rpcRes.getTest());
            ops.put("vars", rpcRes.getVarList());
            ops.put("items", rpcRes.getItemList());
            return ops;
        }

        @Override
        protected ResolveTaskResponse localToRPC_Q(boolean result) {
            return ResolveTaskResponse.newBuilder()
                    .setOpStatus(result ? OPStatus.SUCCEED : OPStatus.FAILD)
                    .build();

        }
    }

    private class ComponentTaskConfigService extends AbstractComponentTaskConfigServer<ResolveContext, TaskTag, ResolveTask> {

        public ComponentTaskConfigService(ResolveContext resolveContext) {
            this.componentContext = resolveContext;
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
        protected <T extends AbstractTask> ResolveTask localToRPC_Q(T config) {
            Task resolveTask = (Task) config;
            if (resolveTask.getVars() == null) {
                resolveTask.setVars(new ArrayList<>());
            }
            if (resolveTask.getItems() == null) {
                resolveTask.setItems(new ArrayList<>());
            }
            return ResolveTask.newBuilder()
                    .setTaskTag(resolveTask.getTaskTag())
                    .setTest(resolveTask.isTest())
                    .addAllVar(
                            resolveTask.getVars().stream()
                                    .map(t -> ResolveTask.Var.newBuilder()
                                            .setName(t.getName())
                                            .setQuery(t.getQuery())
                                            .setOption(t.getOption())
                                            .setOptionValue(t.getOptionValue())
                                            .build()
                                    )
                                    .collect(Collectors.toList())
                    )
                    .addAllItem(
                            resolveTask.getItems().stream()
                                    .map(t -> ResolveTask.Item.newBuilder()
                                            .setItemName(t.getItemName())
                                            .addAllVar(
                                                    t.getVars().stream()
                                                            .map(tx -> ResolveTask.Var.newBuilder()
                                                                    .setName(tx.getName())
                                                                    .setQuery(tx.getQuery())
                                                                    .setOption(tx.getOptionValue())
                                                                    .setOptionValue(tx.getOptionValue())
                                                                    .build()
                                                            )
                                                            .collect(Collectors.toList())
                                            )
                                            .build()
                                    )
                                    .collect(Collectors.toList())

                    )
                    .build();
        }


    }
}
