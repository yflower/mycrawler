package com.jal.crawler.proto;

import com.google.common.util.concurrent.ListenableFuture;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.web.data.enums.StatusEnum;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 通用的组件的rpc客户端
 * Created by jal on 2017/1/28.
 */
public abstract class AbstractComponentClient<C, T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentClient.class);

    protected ManagedChannel channel;

    protected String address;


    //显示组件的运行状态
    public  Optional<StatusEnum> status() {
        Optional<componentStatus> componentStatus = internalStatus();
        if (componentStatus.isPresent()) {
            StatusEnum statusEnum=StatusEnum.valueOf(componentStatus.get().getStatus());
            return Optional.of(statusEnum);
        }else {
            return Optional.empty();
        }
    }

    //组件添加设置
    public boolean setConfig(C config) {
        if (validConfig(config) && internalConfigSet(config)) {
            logger.info("success config {},{}", config.getClass().getSimpleName(), address);
            return true;
        } else {
            logger.info("fail to configSet,check the connection or configSet {},{}", config.getClass().getSimpleName(), address);
            return false;
        }
    }

    //组件显示设置
    public C showConfig() {
        return internalConfigShow(channel);
    }

    //推送任务相关的操作
    public boolean pushTask(T taskOperation) {
        executeTaskOperation(taskOperation);
        return true;
    }

    //显示所有的任务
    public List<internalTask> showTask() {
        Optional<componentStatus> componentStatus = internalStatus();
        if (componentStatus.isPresent()) {
            List<internalTask> internalTasks = componentStatus.get().getStatusTag().entrySet()
                    .stream().map(t -> new internalTask(t.getKey(), t.getValue())).collect(Collectors.toList());
            return internalTasks;
        }
        return new ArrayList<>();
    }

    //关闭client
    public void close() {
        channel.shutdown();
    }

    //得到组件地址
    public String address() {
        return address;
    }


    //private　methods
    //rpc获取组件的状态
    private Optional<componentStatus> internalStatus() {
        ListenableFuture<ComponentStatus> componentStatus = RpcComponentStatusGrpc.newFutureStub(channel).
                rpcComponentStatus(ConfigComponentStatus.getDefaultInstance());
        try {
            ComponentStatus status = componentStatus.get(1, TimeUnit.SECONDS);
            componentStatus internalStatus = new componentStatus();
            internalStatus.setStatus(status.getComponentStatus().name());
            internalStatus.setStatusTag(status.getTasksMap().entrySet()
                    .stream().collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue().name())));
            return Optional.of(internalStatus);
        } catch (InterruptedException e) {
            return Optional.empty();
        } catch (ExecutionException e) {
            return Optional.empty();
        } catch (TimeoutException e) {
            return Optional.empty();
        }

    }

    //执行任务操作 //todo 数据反馈
    private void executeTaskOperation(T taskOperation) {
        if (internalStatus().isPresent() && internalStatus().get().getStatus() == ComponentStatus.Status.STARTED.name()) {
            OPStatus status = taskOperationRequest(taskOperation, channel);
            if (status == OPStatus.SUCCEED) {
                logger.info(" {} add task {},{}", address, taskOperation.getClass().getSimpleName(), taskOperation);
            } else if (status == OPStatus.FAILD) {
                logger.info(" {} fail to add task {},{}", address, taskOperation.getClass().getSimpleName(), taskOperation);
            } else {
                logger.info(" {} already add task {},{}", address, taskOperation.getClass().getSimpleName(), taskOperation);
            }
        } else {
            logger.warn("you must start the component before you start the task,{},{}", taskOperation.getClass().getSimpleName(), address);
        }

    }

    //rpc执行任务操作，返回结果
    private OPStatus taskOperationRequest(T taskOperation, ManagedChannel channel) {
        OPStatus result = OPStatus.FAILD;
        try {
            result = internalTask(taskOperation, channel);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warn("add task fail because the thread is interrupted {},{}", address, taskOperation.getClass().getSimpleName());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }


    //不同的组件内部实现的代码

    protected abstract OPStatus internalTask(T taskOperation, ManagedChannel channel) throws InterruptedException, ExecutionException;

    protected abstract C internalConfigShow(ManagedChannel channel);

    protected abstract boolean internalConfigSet(C config);

    protected abstract boolean validConfig(C config);

    protected abstract TaskType taskType(T task);

    protected abstract String taskTag(T task);


    //组件状态类

    public static class internalTask {
        String taskTag;

        String status;

        public internalTask(String taskTag, String status) {
            this.taskTag = taskTag;
            this.status = status;
        }

        public String getTaskTag() {
            return taskTag;
        }

        public String getStatus() {
            return status;
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
