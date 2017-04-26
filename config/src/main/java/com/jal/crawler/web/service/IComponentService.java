package com.jal.crawler.web.service;

import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;

/**
 * Created by jal on 2017/2/25.
 */
public interface IComponentService {
    boolean component(ComponentRelation componentRelation);

    boolean config(ComponentConfigRelation componentConfigModel);


}
