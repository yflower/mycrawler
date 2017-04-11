package com.jal.crawler.web.service;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.model.task.TaskStatusModel;
import com.jal.crawler.web.data.view.componnet.ComponentVO;
import com.jal.crawler.web.data.view.task.TaskStatusVO;

import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
public interface IComponentStatService {
    Optional<ComponentEnum> type(ComponentModel componentModel);

    Optional<ComponentVO> status(ComponentModel componentModel);

    Optional<TaskStatusVO> taskStatus(String taskTag);
}
