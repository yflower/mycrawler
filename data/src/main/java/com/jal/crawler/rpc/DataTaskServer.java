package com.jal.crawler.rpc;

import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskConfigServer;
import com.cufe.taskProcessor.rpc.server.AbstractComponentTaskServer;
import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.context.DataContext;
import com.jal.crawler.data.DataTypeEnum;
import com.jal.crawler.proto.data.DataTask;
import com.jal.crawler.proto.data.DataTaskResponse;
import com.jal.crawler.proto.data.RpcDataTaskGrpc;
import com.jal.crawler.proto.data.TaskTag;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.task.Task;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jal on 2017/1/23.
 */
@Component
public class DataTaskServer extends RpcDataTaskGrpc.RpcDataTaskImplBase {
    private static final Logger logger = LoggerFactory.getLogger(DataTaskServer.class);

    @Autowired
    private DataContext dataContext;

    @Override
    public void dataTask(DataTask request, StreamObserver<DataTaskResponse> responseObserver) {
        ComponentTaskService taskService = new ComponentTaskService(dataContext);
        responseObserver.onNext(taskService.task(request));
        responseObserver.onCompleted();
    }

    @Override
    public void dataTaskConfig(TaskTag request, StreamObserver<DataTask> responseObserver) {
        ComponentTaskConfigService componentTaskService = new ComponentTaskConfigService(dataContext);
        responseObserver.onNext(componentTaskService.taskConfig(request));
        responseObserver.onCompleted();
    }



    private class ComponentTaskService extends AbstractComponentTaskServer<DataContext, DataTask, DataTaskResponse> {


        public ComponentTaskService(DataContext dataContext) {
            this.componentContext=dataContext;
        }

        @Override
        protected AbstractTask generateTask(String taskTag, Map<String, Object> taskOp) {
            Task task = new Task();
            task.setTaskTag(taskTag);
            task.setTest((Boolean) taskOp.get("test"));
            task.setDataType(DataTypeEnum.numberOf((Integer) taskOp.get("dataType")));
            return task;
        }

        @Override
        protected Map<String, Object> rpcResToLocal(DataTask rpcRes) {
            Map<String, Object> ops = new HashMap();
            ops.put("taskType", rpcRes.getTaskType().getNumber());
            ops.put("taskTag", rpcRes.getTaskTag());
            ops.put("test", rpcRes.getTest());
            ops.put("dataType", rpcRes.getDataTypeValue());
            return ops;
        }

        @Override
        protected DataTaskResponse localToRPC_Q(boolean result) {
            return DataTaskResponse.newBuilder()
                    .setOpStatus(result ? OPStatus.SUCCEED : OPStatus.FAILD)
                    .build();

        }
    }
    private class ComponentTaskConfigService extends AbstractComponentTaskConfigServer<DataContext, TaskTag, DataTask> {

        public ComponentTaskConfigService(DataContext dataContext) {
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
