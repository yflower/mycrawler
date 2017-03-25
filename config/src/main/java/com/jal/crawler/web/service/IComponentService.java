package com.jal.crawler.web.service;

import com.jal.crawler.web.data.model.component.ComponentConfigModel;
import com.jal.crawler.web.data.model.component.ComponentModel;

/**
 * Created by jal on 2017/2/25.
 */
public interface IComponentService {
    boolean component(ComponentModel componentModel);

    boolean config(ComponentConfigModel componentConfigModel);


}
