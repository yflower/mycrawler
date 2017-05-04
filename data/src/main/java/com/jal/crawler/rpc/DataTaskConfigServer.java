package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.DataContext;
import com.jal.crawler.proto.data.DataTask;
import com.jal.crawler.proto.data.RpcDataTaskGrpc;
import com.jal.crawler.proto.data.TaskTag;
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
public class DataTaskConfigServer extends RpcDataTaskGrpc.RpcDataTaskImplBase{

    private static final Logger logger = LoggerFactory.getLogger(DataTaskConfigServer.class);

    @Autowired
    private DataContext dataContext;

    @Override
    public void dataTaskConfig(TaskTag request, StreamObserver<DataTask> responseObserver) {
        ComponentTaskService componentTaskService = new ComponentTaskService(dataContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
        responseObserver.onCompleted();
    }

    private class ComponentTaskService extends AbstractComponentTaskConfigServer<DataContext, TaskTag, DataTask> {

        public ComponentTaskService(DataContext dataContext) {
            this.componentContext = dataContext;
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
        protected <T extends AbstractTask> DataTask localToRPC_Q(T config) {
            Task dataTask = (Task) config;
            return DataTask.newBuilder()
                    .setTaskTag(dataTask.getTaskTag())
                    .setTest(dataTask.isTest())
                    .setDataTypeValue(dataTask.getDataType().getType())
                    .build();
        }



    }


}
