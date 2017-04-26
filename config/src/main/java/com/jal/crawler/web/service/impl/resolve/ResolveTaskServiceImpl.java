package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.rpc.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentRelation;
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
        List<ComponentRelation> componentRelations = resolveTaskLoadService.balanceComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO pause(TaskOperationModel taskOperationModel) {
        List<ComponentRelation> componentRelations = configContext.resolveComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO stop(TaskOperationModel taskOperationModel) {
        List<ComponentRelation> componentRelations = configContext.resolveComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }

    @Override
    public TaskOperationVO destroy(TaskOperationModel taskOperationModel) {
        List<ComponentRelation> componentRelations = configContext.resolveComponent();
        componentOp(componentRelations, taskOperationModel);
        return null;
    }


    private void componentOp(List<ComponentRelation> componentRelations, TaskOperationModel taskOperationModel) {
        componentRelations.forEach(componentModel -> {
            Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentModel);
            AbstractHttpClient abstractComponentClient = clientOptional
                    .orElseThrow(() -> new BizException(ExceptionEnum.ADDRESS_NOT_FOUND));
            resolve(abstractComponentClient, taskOperationModel);
        });
    }

    private void resolve(AbstractHttpClient abstractComponentClient, TaskOperationModel taskOperationModel) {
        ResolveOperationModel model = (ResolveOperationModel) taskOperationModel;
        abstractComponentClient.pushTask(taskOperationModel);


    }


}
