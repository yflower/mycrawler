package com.jal.crawler.web.service;

import com.jal.crawler.web.data.exception.AddressNotFound;
import com.jal.crawler.web.data.exception.ComponentNotFoundException;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.ResolveConfigParam;
import com.jal.crawler.web.data.view.ResolveVO;

import java.util.List;

/**
 * resolve 组件的服务
 * Created by jal on 2017/2/18.
 */
public interface IResolveService {
    List<ResolveVO> status(List<String> address);

    void component(ComponentParam componentParam) throws AddressNotFound;

    void config(ResolveConfigParam resolveConfigParam) throws ComponentNotFoundException, DBConfigException;
}
