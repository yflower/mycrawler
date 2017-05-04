package com.jal.crawler.web.service;

import com.jal.crawler.web.data.model.task.TaskOperationModel;
import com.jal.crawler.web.data.model.task.TaskStatusModel;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.data.view.task.TaskStatusVO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
public interface ITaskService {
    TaskOperationVO push(TaskOperationModel taskOperationModel);

    TaskOperationVO pause(TaskOperationModel taskOperationModel);

    TaskOperationVO stop(TaskOperationModel taskOperationModel);

    TaskOperationVO destroy(TaskOperationModel taskOperationModel);

    TaskOperationVO restart(TaskOperationModel taskOperationModel);

    TaskStatusModel status(String taskTag);

    List<TaskStatusModel> status();

    Optional<ResponseEntity> result(Map<String, Object> param);

    Optional<Map<String,Object>> config(String taskTag);
}
