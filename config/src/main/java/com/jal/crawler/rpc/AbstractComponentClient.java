package com.jal.crawler.rpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.proto.status.Status;
import com.jal.crawler.proto.task.OPStatus;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentStatusModel;
import com.jal.crawler.web.data.model.task.TaskStatusModel;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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
    public Optional<StatusEnum> status() {
        Optional<ComponentStatusModel> componentStatus = internalStatus();
        if (componentStatus.isPresent()) {
            StatusEnum statusEnum = StatusEnum.valueOf(componentStatus.get().getStatus());
            return Optional.of(statusEnum);
        } else {
            return Optional.empty();
        }
    }

    //组件添加设置
    public boolean setConfig(C config) {
        if (validConfig(config) && internalConfigSet(config)) {
            logger.info("成功进行组件设置 {},{}", config.getClass().getSimpleName(), address);
            return true;
        } else {
            logger.warn("[警告],组件设置失败 {},{}", config.getClass().getSimpleName(), address);
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
    public List<TaskStatusModel> showTask() {
        Optional<ComponentStatusModel> componentStatus = internalStatus();
        if (componentStatus.isPresent()) {
            List<TaskStatusModel> internalTasks = componentStatus.get().getTasks();
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
    private Optional<ComponentStatusModel> internalStatus() {
        ListenableFuture<ComponentStatus> future = RpcComponentStatusGrpc.newFutureStub(channel).
                rpcComponentStatus(ConfigComponentStatus.getDefaultInstance());
        try {
            ComponentStatus status = future.get(1, TimeUnit.SECONDS);
            ComponentStatusModel internalStatus = new ComponentStatusModel();
            internalStatus.setStatus(status.getComponentStatus().name());
            internalStatus.setTasks(
                    status.getTasksMap().entrySet()
                            .stream().map(t -> {
                        TaskStatusModel.TaskStatistics taskStatistics = new TaskStatusModel.TaskStatistics();
                        taskStatistics.setStatus(t.getValue().getStatus().name());
                        taskStatistics.setResourceTotal(t.getValue().getResourceTotal());
                        taskStatistics.setProcessorTotal(t.getValue().getProcessorTotal());
                        taskStatistics.setPersistTotal(t.getValue().getPersistTotal());
                        taskStatistics.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getStartTime()), ZoneOffset.systemDefault()));
                        taskStatistics.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getValue().getEndTime()), ZoneOffset.systemDefault()));
                        return new TaskStatusModel(t.getKey(), taskStatistics);
                    }).collect(Collectors.toList()));

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
        if (internalStatus().isPresent() && (internalStatus().get().getStatus() == Status.STARTED.name()
                || internalStatus().get().getStatus() == Status.INIT.name())) {
            OPStatus status = taskOperationRequest(taskOperation, channel);
            if (status == OPStatus.SUCCEED) {
                logger.info("组件添加一个任务{},{}\n{}", address, taskOperation.getClass().getSimpleName(), taskOperation);
            } else if (status == OPStatus.FAILD) {
                logger.info("组件添加一个任务{},{}\n{}", address, taskOperation.getClass().getSimpleName(), taskOperation);
            } else {
                logger.info("任务已经被添加{},{}\n{}", address, taskOperation.getClass().getSimpleName(), taskOperation);
            }
        } else {
            logger.warn("[警告]组件状态异常,无法添加任务,{},{}", taskOperation.getClass().getSimpleName(), address);
        }

    }

    //rpc执行任务操作，返回结果
    private OPStatus taskOperationRequest(T taskOperation, ManagedChannel channel) {
        OPStatus result = OPStatus.FAILD;
        try {
            result = internalTask(taskOperation, channel);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warn("添加组件失败，进程被打断,{},{}", address, taskOperation.getClass().getSimpleName());
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

    protected abstract String taskTag(T task);


    //组件状态类

    public ManagedChannel getChannel() {
        return channel;
    }

    public void setChannel(ManagedChannel channel) {
        this.channel = channel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
