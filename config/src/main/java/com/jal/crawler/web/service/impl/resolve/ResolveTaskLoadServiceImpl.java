package com.jal.crawler.web.service.impl.resolve;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.service.ITaskLoadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jal on 2017/3/19.
 */
@Service("resolveTaskLoadService")
public class ResolveTaskLoadServiceImpl implements ITaskLoadService {
    @Resource
    private ConfigContext configContext;

    //默认只实现了全速下载
    @Override
    public List<ComponentRelation> balanceComponent() {
        return configContext.resolveComponent();
    }
}
