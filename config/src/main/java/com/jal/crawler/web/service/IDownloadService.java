package com.jal.crawler.web.service;

import com.jal.crawler.web.data.exception.AddressNotFound;
import com.jal.crawler.web.data.exception.ComponentNotFoundException;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadConfigParam;
import com.jal.crawler.web.data.view.DownloadVO;

import java.util.List;

/**
 * 下载组件的相关服务
 * Created by jal on 2017/2/18.
 */
public interface IDownloadService {

    List<DownloadVO> status(List<String> address);

    void component(ComponentParam componentParam) throws AddressNotFound;

    void config(DownloadConfigParam downloadConfigParam) throws ComponentNotFoundException, DBConfigException;

}
