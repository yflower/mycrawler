package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.impl.AbstractTaskServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jal on 2017/3/19.
 */
@Service("resolveTaskService")
public class ResolveTaskServiceImpl extends AbstractTaskServiceImpl {

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
        return configContext.resolveComponent();

    }

}
