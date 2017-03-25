package com.jal.crawler.web.biz;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.convert.ComponentConvert;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.ComponentConfigModel;
import com.jal.crawler.web.data.model.ComponentModel;
import com.jal.crawler.web.data.model.configModel.DownloadConfigModel;
import com.jal.crawler.web.data.model.configModel.ResolveConfigModel;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadParam;
import com.jal.crawler.web.data.param.ResolveParam;
import com.jal.crawler.web.data.view.ComponentVO;
import com.jal.crawler.web.service.IComponentService;
import com.jal.crawler.web.service.IComponentStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/25.
 */
@Component
public class ComponentBiz {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentBiz.class);

    @Resource
    private IComponentService downloadService;

    @Resource
    private IComponentService resolveService;

    @Resource
    private IComponentStatService componentStatService;

    @Resource
    private ConfigContext configContext;


    public Map<String, ComponentVO> status(ComponentParam componentParam) {
        List<ComponentModel> componentModels = new ArrayList<>();

        //得到组件model
        if (componentParam != null && !componentModels.isEmpty()) {
            componentModels = componentParam.getComponents().stream()
                    .map(ComponentConvert::paramToModel).collect(Collectors.toList());
        } else {
            componentModels = getCurrentComponentModel();
        }

        Map<String, ComponentVO> result = componentModels.stream()
                .collect(Collectors
                        .toMap(entry -> this.address(entry),
                                entry -> componentStatService.status(entry).orElseGet(ComponentVO::new)));
        LOGGER.info("get component status {}", result);
        return result;

    }


    public void component(ComponentParam componentParam) {
        componentParam.getComponents().forEach(socket -> {
            ComponentModel componentModel = ComponentConvert.paramToModel(socket);
            Optional<ComponentEnum> type = componentStatService.type(componentModel);
            if (!type.isPresent()) {
                throw new BizException(ExceptionEnum.ADDRESS_NOT_FOUND);
            }
            componentModel.setComponentEnum(type.get());
            component(componentModel, () -> {
                if (componentModel.getComponentEnum() == ComponentEnum.DOWNLOAD) {
                    LOGGER.info("add download component {}", componentModel);
                    return defaultDownloadConfig(componentModel);
                } else if (componentModel.getComponentEnum() == ComponentEnum.RESOLVE) {
                    LOGGER.info("add resolve component {}", componentModel);
                    return defaultResolveConfig(componentModel);
                }
                LOGGER.error("error add component,{}", componentModel);
                throw new BizException(ExceptionEnum.UNKNOWN);
            });
        });


    }


    public void component(DownloadParam downloadParam) {


        downloadParam.getDownloads().forEach(t -> {
            ComponentModel componentModel = ComponentConvert.paramToModel(t);
            component(componentModel, () -> ComponentConvert.paramToModel(
                    t,
                    configContext.getRedisConfigModel(),
                    configContext.getMongoConfigModel()));
        });
    }

    public void component(ResolveParam resolveParam) {
        resolveParam.getResolves().forEach(t -> {
            ComponentModel componentModel = ComponentConvert.paramToModel(t);
            component(componentModel, () -> ComponentConvert.paramToModel(
                    t,
                    configContext.getRedisConfigModel(),
                    configContext.getMongoConfigModel()));
        });

    }

    private void component(ComponentModel componentModel, Supplier<ComponentConfigModel> getComponentConfig) {
        internalComponent(componentModel);

        LOGGER.info("add a component {}", componentModel);

        checkDbConfig(configContext);

        ComponentConfigModel componentConfigModel = getComponentConfig.get();

        internalConfig(componentConfigModel);

        LOGGER.info("config a component {}", componentConfigModel.getComponentEnum());
    }


    private void internalComponent(ComponentModel componentModel) {
        boolean result;
        if (componentModel.getComponentEnum() == ComponentEnum.DOWNLOAD) {
            result = downloadService.component(componentModel);
        } else {
            result = resolveService.component(componentModel);
        }
        if (!result) {
            LOGGER.warn("can't find address component={}", componentModel);
            throw new BizException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
    }


    private void internalConfig(ComponentConfigModel componentConfigModel) {
        boolean result;
        //组件设置
        if (componentConfigModel.getComponentEnum() == ComponentEnum.DOWNLOAD) {
            result = downloadService.config(componentConfigModel);
        } else {
            result = resolveService.config(componentConfigModel);
        }
        if (!result) {
            LOGGER.warn("config error component={}", componentConfigModel);
            throw new BizException(ExceptionEnum.CONFIG_ERROR);
        }
    }


    private void checkDbConfig(ConfigContext configContext) {
        if (configContext.getRedisConfigModel() == null || configContext.getMongoConfigModel() == null) {
            LOGGER.warn("you must config you db before you config you component");
            throw new BizException(ExceptionEnum.DB_CONFIG_ERROR);
        }
    }


    private String address(ComponentModel componentModel) {
        return componentModel.getHost() + ":" + componentModel.getPort();
    }


    /**
     * 得到当前所有可用的组件
     *
     * @return
     */
    private List<ComponentModel> getCurrentComponentModel() {
        List<ComponentModel> result = new ArrayList<>();
        result.addAll(configContext.resolveComponent());
        result.addAll(configContext.downloadComponent());
        return result;

    }


    /**
     * 默认的组件设置
     *
     * @param componentModel
     * @return
     */
    private ComponentConfigModel defaultDownloadConfig(ComponentModel componentModel) {
        DownloadConfigModel downloadConfigModel = new DownloadConfigModel();
        downloadConfigModel.setHost(componentModel.getHost());
        downloadConfigModel.setPort(componentModel.getPort());
        downloadConfigModel.setComponentEnum(componentModel.getComponentEnum());
        downloadConfigModel.setThread(2);
        downloadConfigModel.setSleepTime(100);
        downloadConfigModel.setMongoConfigModel(configContext.getMongoConfigModel());
        downloadConfigModel.setRedisConfigModel(configContext.getRedisConfigModel());
        return downloadConfigModel;
    }

    private ComponentConfigModel defaultResolveConfig(ComponentModel componentModel) {
        ResolveConfigModel resolveConfigModel = new ResolveConfigModel();
        resolveConfigModel.setComponentEnum(componentModel.getComponentEnum());
        resolveConfigModel.setHost(componentModel.getHost());
        resolveConfigModel.setPort(componentModel.getPort());
        resolveConfigModel.setThread(2);
        resolveConfigModel.setMongoConfigModel(configContext.getMongoConfigModel());
        resolveConfigModel.setRedisConfigModel(configContext.getRedisConfigModel());
        return resolveConfigModel;
    }


}
