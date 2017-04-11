package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.rpc.AbstractComponentClient;
import com.jal.crawler.web.convert.RpcEnumConvert;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.model.task.ResolveOperationModel;
import com.jal.crawler.web.data.model.task.TaskOperationModel;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.service.ITaskLoadService;
import com.jal.crawler.web.service.ITaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/3/19.
 */
@Service("resolveTaskService")
public class ResolveTaskServiceImpl implements ITaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveTaskServiceImpl.class);

    @Resource
    private ConfigContext configContext;

    @Resource
    private ITaskLoadService resolveTaskLoadService;


    @Override
    public TaskOperationVO push(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = resolveTaskLoadService.balanceComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO pause(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = configContext.resolveComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO stop(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = configContext.resolveComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO destroy(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = configContext.resolveComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }


    private void componentOp(List<ComponentModel> componentModels, TaskOperationModel taskOperationModel) {
        componentModels.forEach(componentModel -> {
            Optional<AbstractComponentClient> clientOptional = configContext.getRpcClient().getClient(componentModel);
            AbstractComponentClient abstractComponentClient = clientOptional
                    .orElseThrow(() -> new BizException(ExceptionEnum.ADDRESS_NOT_FOUND));
            resolve(abstractComponentClient, taskOperationModel);
        });
    }

    private void resolve(AbstractComponentClient abstractComponentClient, TaskOperationModel taskOperationModel) {
        ResolveOperationModel model = (ResolveOperationModel) taskOperationModel;
        abstractComponentClient.pushTask(
                ResolveTask.newBuilder()
                        .setTaskTag(model.getTaskTag())
                        .setTest(model.isTest())
                        .setTaskType(RpcEnumConvert.taskOperationType(model.getType()))
                        .addAllVar(
                                model.getVars().stream()
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
                                model.getItems().stream()
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
                        .build()
        );


    }


}
