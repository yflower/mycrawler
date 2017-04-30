package com.jal.crawler.web.service;

import com.jal.crawler.web.data.model.task.TaskOperationModel;
import com.jal.crawler.web.data.model.task.TaskStatusModel;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.data.view.task.TaskStatusVO;

import java.util.List;

/**
 * Created by jal on 2017/3/19.
 */
public interface ITaskService {
    TaskOperationVO push(TaskOperationModel taskOperationModel);

    TaskOperationVO pause(TaskOperationModel taskOperationModel);

    TaskOperationVO stop(TaskOperationModel taskOperationModel);

    TaskOperationVO destroy(TaskOperationModel taskOperationModel);

    TaskStatusModel status(String taskTag);

    List<TaskStatusModel> status();
}
