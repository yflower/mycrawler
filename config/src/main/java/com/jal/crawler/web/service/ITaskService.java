package com.jal.crawler.web.service;

import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.model.TaskOperationModel;
import com.jal.crawler.web.data.view.TaskOperationVO;

/**
 * Created by jal on 2017/3/19.
 */
public interface ITaskService {
    TaskOperationVO push(TaskOperationModel taskOperationModel);

    TaskOperationVO pause(TaskOperationModel taskOperationModel);

    TaskOperationVO stop(TaskOperationModel taskOperationModel);

    TaskOperationVO destroy(TaskOperationModel taskOperationModel);
}
