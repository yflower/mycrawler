package com.jal.crawler.web.service.impl;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.http.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.task.TaskOperationModel;
import com.jal.crawler.web.data.model.task.TaskStatusModel;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.ITaskService;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/8.
 */
public abstract class AbstractTaskServiceImpl implements ITaskService {
    protected ConfigContext configContext;

    protected IComponentSelectService componentSelectService;

    @Override
    public TaskOperationVO push(TaskOperationModel taskOperationModel) {
        return taskOp(taskOperationModel);
    }

    @Override
    public TaskOperationVO pause(TaskOperationModel taskOperationModel) {
        return taskOp(taskOperationModel);
    }

    @Override
    public TaskOperationVO stop(TaskOperationModel taskOperationModel) {
        return taskOp(taskOperationModel);
    }

    @Override
    public TaskOperationVO destroy(TaskOperationModel taskOperationModel) {
        return taskOp(taskOperationModel);
    }

    @Override
    public TaskOperationVO restart(TaskOperationModel taskOperationModel) {
        return taskOp(taskOperationModel);
    }

    @Override
    public Optional<ResponseEntity> result(Map<String, Object> param) {
        return taskResult(param);
    }

    @Override
    public Optional<Map<String, Object>> config(String taskTag) {
        return taskConfigInfo(taskTag);
    }

    @Override
    public TaskStatusModel status(String taskTag) {
        List<TaskStatusModel> list = taskStatus(taskTag);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }



    @Override
    public List<TaskStatusModel> status() {
        return taskStatus(null);
    }

    private List<TaskStatusModel> taskStatus(String taskTag) {
        List<TaskStatusModel> result ;
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(availableComponents());
        if (relationOptional.isPresent()) {
            AbstractHttpClient client = client(relationOptional.get());
            if (taskTag == null) {
                Optional optional = client.taskStatus();
                if (optional.isPresent()) {
                    return (List<TaskStatusModel>) optional.get();
                }

            } else {
                result = new ArrayList<>();
                Optional optional = client.taskStatus(taskTag);
                if (optional.isPresent()) {
                    result.add((TaskStatusModel) optional.get());
                }
            }

        } else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "没有找到可以执行任务的组件");
        }
        return new ArrayList<>();
    }

    private TaskOperationVO taskOp(TaskOperationModel taskOperationModel) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(availableComponents());
        if (relationOptional.isPresent()) {
            boolean result = client(relationOptional.get()).pushTask(taskOperationModel);
            TaskOperationVO taskOperationVO = new TaskOperationVO();
            if (result) {
                taskOperationVO.setTaskTag(taskOperationModel.getTaskTag());
                return taskOperationVO;
            } else {
                return taskOperationVO;
            }
        } else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "没有找到可以执行任务的组件");
        }
    }

    private Optional<Map<String, Object>> taskConfigInfo(String taskTag) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(availableComponents());
        if (relationOptional.isPresent()) {
            return client(relationOptional.get()).taskConfig(taskTag);
        } else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "没有找到可以执行任务的组件");
        }
    }

    private Optional<ResponseEntity> taskResult(Map<String, Object> param) {
        Optional<ComponentRelation> relationOptional = componentSelectService.selectComponent(availableComponents());
        if (relationOptional.isPresent()) {
            return client(relationOptional.get()).result(param);

        } else {
            throw new BizException(ExceptionEnum.COMPONENT_NOT_FOUND, "没有找到可以执行任务的组件");
        }
    }

    private AbstractHttpClient client(ComponentRelation componentRelation) {
        Optional<AbstractHttpClient> clientOptional = configContext.getRpcClient().getClient(componentRelation);
        AbstractHttpClient abstractComponentClient = clientOptional
                .orElseThrow(() -> new BizException(ExceptionEnum.ADDRESS_NOT_FOUND));
        return abstractComponentClient;

    }


    protected abstract List<ComponentRelation> availableComponents();


}
