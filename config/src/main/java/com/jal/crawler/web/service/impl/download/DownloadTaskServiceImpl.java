package com.jal.crawler.web.service.impl.download;

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
import com.jal.crawler.web.service.impl.AbstractTaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadTaskService")
public class DownloadTaskServiceImpl extends AbstractTaskServiceImpl {

    @Resource
    public void setConfigContext(ConfigContext configContext) {
        this.configContext = configContext;
    }

    @Resource
    public void setComponentSelectService(IComponentSelectService componentSelectService) {
        this.componentSelectService = componentSelectService;
    }

    @Override
    protected List<ComponentRelation> availableComponents() {
        return configContext.downloadComponent();

    }
}
