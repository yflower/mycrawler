package com.jal.crawler.web.biz;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.convert.ComponentConvert;
import com.jal.crawler.web.data.constants.DefaultConfigModelConstant;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentConfigModel;
import com.jal.crawler.web.data.model.component.ComponentModel;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadParam;
import com.jal.crawler.web.data.param.ResolveParam;
import com.jal.crawler.web.data.view.componnet.ComponentVO;
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
 * 组件相关的业务
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

        Map<String, ComponentVO> result =
                componentModels.stream()
                        .collect(Collectors
                                .toMap(this::address,
                                        entry -> componentStatService.status(entry).orElseGet(ComponentVO::new)));
        LOGGER.info("获取组件状态", result);
        return result;

    }


    public void component(ComponentParam componentParam) {
        componentParam.getComponents().forEach(socket -> {
            ComponentModel componentModel = ComponentConvert.paramToModel(socket);
            Optional<ComponentEnum> type = componentStatService.type(componentModel);
            if (!type.isPresent()) {
                throw new BizException(ExceptionEnum.ADDRESS_NOT_FOUND,socket.toString());
            }
            componentModel.setComponentEnum(type.get());
            component(componentModel, () -> {
                if (componentModel.getComponentEnum() == ComponentEnum.DOWNLOAD) {
                    LOGGER.info("添加下载组件 {}", componentModel);
                    return DefaultConfigModelConstant.defaultDownloadConfig(componentModel,
                            configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
                } else if (componentModel.getComponentEnum() == ComponentEnum.RESOLVE) {
                    LOGGER.info("添加解析组件 {}", componentModel);
                    return DefaultConfigModelConstant.defaultResolveConfig(componentModel,
                            configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
                }
                LOGGER.warn("[警告]添加组件 {}", componentModel);
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

        checkDbConfig(configContext);

        ComponentConfigModel componentConfigModel = getComponentConfig.get();

        internalConfig(componentConfigModel);

    }


    private void internalComponent(ComponentModel componentModel) {
        boolean result;
        if (componentModel.getComponentEnum() == ComponentEnum.DOWNLOAD) {
            result = downloadService.component(componentModel);
        } else {
            result = resolveService.component(componentModel);
        }
        if (!result) {
            LOGGER.warn("[警告] 没有找到组件的地址 {}", componentModel);
            throw new BizException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
    }


    private void internalConfig(ComponentConfigModel componentConfigModel) {
        boolean result;
        //组件设置
        if (componentConfigModel.getComponentEnum() == ComponentEnum.DOWNLOAD) {
            result = downloadService.config(componentConfigModel);
            LOGGER.info("下载组件设置成功");
        } else {
            result = resolveService.config(componentConfigModel);
            LOGGER.info("解析组件设置成功");
        }
        if (!result) {
            LOGGER.warn("[警告] 组件设置失败 {}", componentConfigModel);
            throw new BizException(ExceptionEnum.CONFIG_ERROR);
        }
    }


    private void checkDbConfig(ConfigContext configContext) {
        if (configContext.getRedisConfigModel() == null || configContext.getMongoConfigModel() == null) {
            LOGGER.warn("[警告]你必须先设置数据库");
            throw new BizException(ExceptionEnum.DB_CONFIG_ERROR);
        }
    }


    private String address(ComponentModel componentModel) {
        return componentModel.getHost() + ":" + componentModel.getPort();
    }


    /**
     * 得到当前所有可用的组件
     *
     * @return 返回当前可用所有的组件
     */
    private List<ComponentModel> getCurrentComponentModel() {
        List<ComponentModel> result = new ArrayList<>();
        result.addAll(configContext.resolveComponent());
        result.addAll(configContext.downloadComponent());
        return result;

    }


}
