package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.resolve.TaskTag;
import com.jal.crawler.proto.task.TaskType;
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
public class ResolveTaskConfigServer extends RpcResolveTaskGrpc.RpcResolveTaskImplBase {

    private static final Logger logger = LoggerFactory.getLogger(ResolveTaskConfigServer.class);

    @Autowired
    private ResolveContext resolveContext;

    @Override
    public void resolveTaskConfig(TaskTag request, StreamObserver<ResolveTask> responseObserver) {
        ComponentTaskService componentTaskService = new ComponentTaskService(resolveContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
        responseObserver.onCompleted();
    }

    private class ComponentTaskService extends AbstractComponentTaskConfigServer<ResolveContext, TaskTag, ResolveTask> {

        public ComponentTaskService(ResolveContext resolveContext) {
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
                                                    t.getItemVar().stream()
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
