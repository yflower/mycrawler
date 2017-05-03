package com.jal.crawler.web.biz;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.convert.ComponentConvert;
import com.jal.crawler.web.data.constants.DefaultConfigModelConstant;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.model.component.ComponentConfigRelation;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadParam;
import com.jal.crawler.web.data.param.ResolveParam;
import com.jal.crawler.web.data.view.componnet.ComponentVO;
import com.jal.crawler.web.service.IComponentSelectService;
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
    private IComponentService dataService;

    @Resource
    private IComponentStatService componentStatService;

    @Resource
    private ConfigContext configContext;

    @Resource
    private IComponentSelectService selectService;


    public Map<String, ComponentVO> status(ComponentParam componentParam) {
        List<ComponentRelation> componentRelations = new ArrayList<>();

        //得到组件model
        if (componentParam != null) {
            componentRelations = componentParam.getComponents().stream()
                    .map(ComponentConvert::paramToModel).collect(Collectors.toList());
        } else {
            componentRelations = getCurrentComponentModel();
        }

        Map<String, ComponentVO> result =
                componentRelations.stream()
                        .collect(Collectors
                                .toMap(this::address,
                                        entry -> componentStatService.status(entry).orElseGet(ComponentVO::new)));
        LOGGER.info("获取组件状态", result);
        return result;

    }


    public void component(ComponentParam componentParam) {
        componentParam.getComponents().forEach(socket -> {
            ComponentRelation componentRelation = ComponentConvert.paramToModel(socket);
            Optional<ComponentEnum> type = componentStatService.type(componentRelation);
            if (!type.isPresent()) {
                throw new BizException(ExceptionEnum.ADDRESS_NOT_FOUND, socket.toString());
            }
            componentRelation.setComponentType(type.get().getCode());
            component(componentRelation, () -> {
                if (componentRelation.getComponentType() == ComponentEnum.DOWNLOAD.getCode()) {
                    LOGGER.info("添加 download 组件 {}", componentRelation);
                    return DefaultConfigModelConstant.defaultDownloadConfig(componentRelation,
                            configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
                } else if (componentRelation.getComponentType() == ComponentEnum.RESOLVE.getCode()) {
                    LOGGER.info("添加 resolve 组件 {}", componentRelation);
                    return DefaultConfigModelConstant.defaultResolveConfig(componentRelation,
                            configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
                } else if (componentRelation.getComponentType() == ComponentEnum.DATA.getCode()) {
                    return DefaultConfigModelConstant.defaultDataConfig(componentRelation,
                            configContext.getRedisConfigModel(), configContext.getMongoConfigModel());
                }

                LOGGER.warn("[警告]添加组件,未知的组件类型{}", componentRelation);
                throw new BizException(ExceptionEnum.UNKNOWN);
            });
        });


    }


    public void component(DownloadParam downloadParam) {
        downloadParam.getDownloads().forEach(t -> {
            ComponentRelation componentRelation = ComponentConvert.paramToModel(t);
            component(componentRelation, () -> ComponentConvert.paramToModel(
                    t,
                    configContext.getRedisConfigModel(),
                    configContext.getMongoConfigModel()));
        });
    }

    public void component(ResolveParam resolveParam) {
        resolveParam.getResolves().forEach(t -> {
            ComponentRelation componentRelation = ComponentConvert.paramToModel(t);
            component(componentRelation, () -> ComponentConvert.paramToModel(
                    t,
                    configContext.getRedisConfigModel(),
                    configContext.getMongoConfigModel()));
        });

    }

    private void component(ComponentRelation componentRelation, Supplier<ComponentConfigRelation> getComponentConfig) {
        internalComponent(componentRelation);

        checkDbConfig(configContext);

        ComponentConfigRelation componentConfigModel = getComponentConfig.get();

        internalConfig(componentConfigModel);

    }


    private void internalComponent(ComponentRelation componentRelation) {
        boolean result;
        if (componentRelation.getComponentType() == ComponentEnum.DOWNLOAD.getCode()) {
            result = downloadService.component(componentRelation);
        } else if (componentRelation.getComponentType() == ComponentEnum.RESOLVE.getCode()) {
            result = resolveService.component(componentRelation);
        } else if (componentRelation.getComponentType() == ComponentEnum.DATA.getCode()) {
            result = dataService.component(componentRelation);
        } else {
            throw new IllegalStateException("无法解析组件类型");
        }
        if (!result) {
            LOGGER.warn("[警告] 没有找到组件的地址 {}", componentRelation);
            throw new BizException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
    }


    private void internalConfig(ComponentConfigRelation componentConfigModel) {
        boolean result;
        //组件设置
        if (componentConfigModel.getComponentType() == ComponentEnum.DOWNLOAD.getCode()) {
            result = downloadService.config(componentConfigModel);
            LOGGER.info("下载组件设置成功");
        } else if (componentConfigModel.getComponentType() == ComponentEnum.RESOLVE.getCode()) {
            result = resolveService.config(componentConfigModel);
            LOGGER.info("解析组件设置成功");
        } else if (componentConfigModel.getComponentType() == ComponentEnum.DATA.getCode()) {
            result = dataService.config(componentConfigModel);
        } else {
            throw new IllegalStateException("无法解析组件类型");
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


    private String address(ComponentRelation componentRelation) {
        return componentRelation.getHost() + ":" + componentRelation.getPort();
    }


    /**
     * 得到当前所有可用的组件
     *
     * @return 返回当前可用所有的组件
     */
    private List<ComponentRelation> getCurrentComponentModel() {
        List<ComponentRelation> result = new ArrayList<>();
        result.addAll(configContext.resolveComponent());
        result.addAll(configContext.downloadComponent());
        return result;

    }


}
