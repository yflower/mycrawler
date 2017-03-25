package com.jal.crawler.web.service;

import com.jal.crawler.proto.AbstractComponentClient;
import com.jal.crawler.web.data.model.ComponentConfigModel;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.model.TaskOperationModel;
import com.jal.crawler.web.data.view.ComponentVO;
import com.jal.crawler.web.data.view.TaskOperationVO;

import java.util.Optional;

/**
 * Created by jal on 2017/2/25.
 */
public interface IComponentService {
    boolean component(ComponentModel componentModel);

    boolean config(ComponentConfigModel componentConfigModel);


}
