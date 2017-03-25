package com.jal.crawler.web.service.impl.download;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.service.ITaskLoadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jal on 2017/3/19.
 */
@Service("downloadTaskLoadService")
public class DownloadTaskLoadServiceImpl implements ITaskLoadService {
    @Resource
    private ConfigContext configContext;

    //默认只实现了全速下载
    @Override
    public List<ComponentModel> balanceComponent() {

        return configContext.downloadComponent();

    }
}
