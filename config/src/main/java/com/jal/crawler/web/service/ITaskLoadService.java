package com.jal.crawler.web.service;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.ComponentModel;

import java.util.List;

/**
 * Created by jal on 2017/3/19.
 */
public interface ITaskLoadService {
    List<ComponentModel> balanceComponent();
}
