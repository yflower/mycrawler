package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.task.DownloadOperationModel;
import com.jal.crawler.web.data.model.task.TaskOperationModel;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.service.ITaskLoadService;
import com.jal.crawler.web.service.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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
        List<ComponentRelation> componentRelations = downloadTaskLoadService.balanceComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO pause(TaskOperationModel taskOperationModel) {
        List<ComponentRelation> componentRelations = configContext.downloadComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO stop(TaskOperationModel taskOperationModel) {
        List<ComponentRelation> componentRelations = configContext.downloadComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO destroy(TaskOperationModel taskOperationModel) {
        List<ComponentRelation> componentRelations = configContext.downloadComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }


    private void componentOp(List<ComponentRelation> componentRelations, TaskOperationModel taskOperationModel) {
        componentRelations.forEach(componentModel -> {
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentModel);
            AbstractHttpClient abstractComponentClient = clientOptional
                    .orElseThrow(() -> new BizException(ExceptionEnum.ADDRESS_NOT_FOUND));
            download(abstractComponentClient, taskOperationModel);
        });
    }

    private void download(AbstractHttpClient abstractComponentClient, TaskOperationModel taskOperationModel) {
        DownloadOperationModel model = (DownloadOperationModel) taskOperationModel;

        abstractComponentClient.pushTask(taskOperationModel);
    }

}
