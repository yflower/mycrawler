package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.task.TaskOperationModel;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadTaskService")
public class DownloadTaskServiceImpl implements ITaskService {
    @Resource
    private ConfigContext configContext;

    @Resource
    private IComponentSelectService componentSelectService;


    @Override
    public TaskOperationVO push(TaskOperationModel taskOperationModel) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(configContext.downloadComponent());
        if(relationOptional.isPresent()){
            componentOp(relationOptional.get(), taskOperationModel);

        }else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND,"没有找到可以执行任务的组件");
        }
        return null;
    }

    @Override
    public TaskOperationVO pause(TaskOperationModel taskOperationModel) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(configContext.downloadComponent());
        if(relationOptional.isPresent()){
            componentOp(relationOptional.get(), taskOperationModel);

        }else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND,"没有找到可以执行任务的组件");
        }
        return null;
    }

    @Override
    public TaskOperationVO stop(TaskOperationModel taskOperationModel) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(configContext.downloadComponent());
        if(relationOptional.isPresent()){
            componentOp(relationOptional.get(), taskOperationModel);

        }else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND,"没有找到可以执行任务的组件");
        }
        return null;
    }

    @Override
    public TaskOperationVO destroy(TaskOperationModel taskOperationModel) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(configContext.downloadComponent());
        if(relationOptional.isPresent()){
            componentOp(relationOptional.get(), taskOperationModel);

        }else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND,"没有找到可以执行任务的组件");
        }
        return null;
    }


    private void componentOp(ComponentRelation componentRelation, TaskOperationModel taskOperationModel) {
        Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentRelation);
        AbstractHttpClient abstractComponentClient = clientOptional
                .orElseThrow(() -> new BizException(ExceptionEnum.ADDRESS_NOT_FOUND));
        download(abstractComponentClient, taskOperationModel);

    }

    private void download(AbstractHttpClient abstractComponentClient, TaskOperationModel taskOperationModel) {
        abstractComponentClient.pushTask(taskOperationModel);
    }

}
