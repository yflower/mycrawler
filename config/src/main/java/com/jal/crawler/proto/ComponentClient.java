package com.jal.crawler.proto;

import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.proto.task.TaskType;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/1/28.
 */
public abstract class ComponentClient<C, T> {
    private static final Logger logger = LoggerFactory.getLogger(ComponentClient.class);

    ManagedChannel channel;

    String address;


    //public client method
    public String status() {
        return internalStatus().getStatus();
    }

    public ComponentClient setConfig(C config) {
        if (validConfig(config) && internalConfigSet(config)) {
            logger.info("success configSet {},{}", config.getClass(), address);
        } else {
            logger.info("fail to configSet,check the connection or configSet {},{}", config.getClass(), address);
        }
        return this;
    }

    public C showConfig() {
        return internalConfigShow(channel);
    }

    public ComponentClient pushTask(T task) {
        typeTask(task);
        return this;
    }

    public List<internalTask> showTask() {
        componentStatus componentStatus = internalStatus();
        List<internalTask> internalTasks = componentStatus.getStatusTag().entrySet()
                .stream().map(t -> new internalTask(t.getKey(), t.getValue())).collect(Collectors.toList());
        return internalTasks;
    }


    public void close() {
        channel.shutdown();
    }

    public String address() {
        return address;
    }


    //private　methods
    private componentStatus internalStatus() {
        ComponentStatus componentStatus = RpcComponentStatusGrpc.newBlockingStub(channel).
                rpcComponentStatus(ConfigComponentStatus.getDefaultInstance());
        componentStatus internalStatus = new componentStatus();
        internalStatus.setStatus(componentStatus.getComponentStatus().name());
        internalStatus.setStatusTag(componentStatus.getTasksMap().entrySet()
                .stream().collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue().name())));
        return internalStatus;
    }


    private void typeTask(T task) {
        if (internalStatus().getStatus() == ComponentStatus.Status.STARTED.name()) {
            OPStatus status = executeTaskRequest(task, channel);
            if (status == OPStatus.SUCCEED) {
                logger.info(" {} add task {},{}", address, task.getClass(), task);
            } else if (status == OPStatus.FAILD) {
                logger.info(" {} fail to add task {},{}", address, task.getClass(), task);
            } else {
                logger.info(" {} already add task {},{}", address, task.getClass(), task);
            }
        } else {
            logger.warn("you must start the component before you start the task,{},{}", task.getClass(), address);
        }

    }

    private OPStatus executeTaskRequest(T task, ManagedChannel channel) {
        OPStatus result = OPStatus.FAILD;
        try {
            result = internalTask(task, channel);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warn("add task fail because the thread is interrupted {},{}", address, task.getClass());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected abstract OPStatus internalTask(T task, ManagedChannel channel) throws InterruptedException, ExecutionException;

    protected abstract C internalConfigShow(ManagedChannel channel);

    protected abstract boolean internalConfigSet(C config);

    protected abstract boolean validConfig(C config);

    protected abstract TaskType taskType(T task);

    protected abstract String taskTag(T task);



    //组件状态类

    public static class internalTask {
        String taskTag;

        String status;

        public String getTaskTag() {
            return taskTag;
        }

        public String getStatus() {
            return status;
        }

        public internalTask(String taskTag, String status) {
            this.taskTag = taskTag;
            this.status = status;
        }
    }


    public static class componentStatus {
        private String status;

        private Map<String, String> statusTag;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Map<String, String> getStatusTag() {
            return statusTag;
        }

        public void setStatusTag(Map<String, String> statusTag) {
            this.statusTag = statusTag;
        }
    }
}
