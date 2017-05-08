package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.http.AbstractHttpClient;
import com.jal.crawler.web.data.enums.ComponentRelationTypeEnum;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.service.IComponentSelectService;
import com.jal.crawler.web.service.IComponentService;
import com.jal.crawler.web.service.impl.AbstractComponentServiceImpl;
import com.jal.crawler.web.service.impl.download.DownloadServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by jal on 2017/3/19.
 */
@Service("resolveService")
public class ResolveServiceImpl extends AbstractComponentServiceImpl {
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
