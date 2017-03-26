package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.task.TaskType;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.enums.TaskOperationEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.model.taskOperation.DownloadOperationModel;
import com.jal.crawler.web.data.model.taskOperation.TaskOperationModel;
import com.jal.crawler.web.data.view.TaskOperationVO;
import com.jal.crawler.web.service.ITaskLoadService;
import com.jal.crawler.web.service.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadTaskService")
public class DownloadTaskServiceImpl implements ITaskService {
    @Resource
    private ConfigContext configContext;

    @Resource
    private ITaskLoadService downloadTaskLoadService;


    @Override
    public TaskOperationVO push(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = downloadTaskLoadService.balanceComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO pause(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = configContext.downloadComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO stop(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = configContext.downloadComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO destroy(TaskOperationModel taskOperationModel) {
        List<ComponentModel> componentModels = configContext.downloadComponent();
        componentOp(componentModels, taskOperationModel);
        return null;
    }


    private void componentOp(List<ComponentModel> componentModels, TaskOperationModel taskOperationModel) {
        componentModels.forEach(componentModel -> {
            Optional<AbstractComponentClient> clientOptional = configContext.getRpcClient().getClient(componentModel);
            AbstractComponentClient abstractComponentClient = clientOptional
                    .orElseThrow(() -> new BizException(ExceptionEnum.ADDRESS_NOT_FOUND));
            download(abstractComponentClient, taskOperationModel);
        });
    }

    private void download(AbstractComponentClient abstractComponentClient, TaskOperationModel taskOperationModel) {
        DownloadOperationModel model = (DownloadOperationModel) taskOperationModel;

        abstractComponentClient.pushTask(
                DownloadTask.newBuilder()
                        .setTaskType(this.enumToRpcEnum(model.getType()))
                        .setTaskTag(model.getTaskTag())
                        .setDynamic(model.isDynamic())
                        .setTest(model.isTest())
                        .addAllStartUrl(model.getUrls())
                        .addAllPre(
                                model.getPreProcess().stream()
                                        .map(t -> DownloadTask.Processor.newBuilder()
                                                .setOrder(t.getOrder())
                                                .setType(this.downloadOperationTypeToRPCDownloadProcessType(t.getType()))
                                                .setQuery(t.getQuery())
                                                .setValue(t.getValue())
                                                .build()
                                        ).collect(Collectors.toList())
                        )
                        .addAllPost(
                                model.getPostProcess().stream()
                                        .map(t -> DownloadTask.Processor.newBuilder()
                                                .setOrder(t.getOrder())
                                                .setType(this.downloadOperationTypeToRPCDownloadProcessType(t.getType()))
                                                .setQuery(t.getQuery())
                                                .setValue(t.getValue())
                                                .build()
                                        ).collect(Collectors.toList())
                        )
                        .addAllStartUrl(model.getUrls())
                        .build()
        );
    }

    private TaskType enumToRpcEnum(TaskOperationEnum taskOperationEnum) {
        switch (taskOperationEnum) {
            case ADD:
                return TaskType.ADD;
            case DESTROY:
                return TaskType.DESTROY;
            case FINISH:
                return TaskType.FINISH;
            case STOP:
                return TaskType.STOP;
            case UPDATE:
                return TaskType.UPDATE;
            default:
                return null;
        }
    }

    private DownloadTask.Processor.Type downloadOperationTypeToRPCDownloadProcessType(DownloadOperationModel.process.type type) {
        switch (type) {
            case CLICK:
                return DownloadTask.Processor.Type.CLICK;
            case INPUT:
                return DownloadTask.Processor.Type.INPUT;
            case INPUT_SUBMIT:
                return DownloadTask.Processor.Type.INPUT_SUBMIT;
            case LINK_TO:
                return DownloadTask.Processor.Type.LINK_TO;
            case WAIT_UTIL:
                return DownloadTask.Processor.Type.WAIT_UTIL;
            default:
                return null;
        }
    }
}
