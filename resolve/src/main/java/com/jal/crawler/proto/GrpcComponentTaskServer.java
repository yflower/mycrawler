package com.jal.crawler.proto;

import com.cufe.taskProcessor.enums.TaskTypeEnum;
import com.cufe.taskProcessor.service.AbstractComponentTaskService;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.proto.resolve.ResolveTaskResponse;
import com.jal.crawler.proto.resolve.RpcResolveTaskGrpc;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by jal on 2017/1/23.
 */
@Component
public class GrpcComponentTaskServer extends RpcResolveTaskGrpc.RpcResolveTaskImplBase {
    private static final Logger logger = LoggerFactory.getLogger(GrpcComponentTaskServer.class);

    @Autowired
    private ResolveContext resolveContext;

    @Override
    public void resolveTask(ResolveTask request, StreamObserver<ResolveTaskResponse> responseObserver) {
        ComponentTaskService taskService = new ComponentTaskService();
        taskService.setComponentContext(resolveContext);

        List<Task.var> vars = new ArrayList<>();
        request.getVarList().stream().forEach(t -> vars.add(new Task.var(t.getName(), t.getQuery(), t.getOption(), t.getOptionValue())));

        List<Task.item> items = new ArrayList<>();
        request.getItemList().stream().forEach(t -> {
            Task.item item = new Task.item(t.getItemName(),
                    t.getVarList().stream().map(
                            var -> new Task.var(var.getName(),
                                    var.getQuery(), var.getOption(),
                                    var.getOptionValue()))
                            .collect(Collectors.toList()));
            items.add(item);
        });

        taskService.vars = vars;
        taskService.items = items;
        taskService.test=request.getTest();

        boolean task = taskService.task(request.getTaskTag(), TaskTypeEnum.numberOf(request.getTaskTypeValue()));


        ResolveTaskResponse.Builder builder = ResolveTaskResponse.newBuilder();
        builder.setOpStatus(task ? OPStatus.SUCCEED : OPStatus.FAILD);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }


    private class ComponentTaskService extends AbstractComponentTaskService {
        List<Task.var> vars;
        List<Task.item> items;
        boolean test;


        @Override
        public AbstractTask generateTask(String taskTag) {
            Task task = new Task();
            task.setTaskTag(taskTag);
            task.setTest(test);
            task.setItems(items);
            task.setVars(vars);
            return task;
        }
    }
}
