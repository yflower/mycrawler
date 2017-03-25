package com.jal.crawler.web.service;

import com.jal.crawler.proto.status.ComponentType;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.view.ComponentVO;

import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
public interface IComponentStatService {
     Optional<ComponentEnum> type(ComponentModel componentModel);

     Optional<ComponentVO> status(ComponentModel componentModel);
}
