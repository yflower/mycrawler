package com.jal.crawler.rpc;

import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by jianganlan on 2017/4/26.
 */
public abstract class AbstractHttpClient<C, T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClient.class);

    protected RestTemplate restTemplate;

    protected ComponentRelation componentRelation;

    public Optional<ComponentRelation> status() {
        String url = "http://" + this.componentRelation.getHost() + ":8080/component/status";
        ResponseEntity<ComponentRelation> entity;
        try {
            entity = restTemplate.getForEntity(url, ComponentRelation.class);
        } catch (Exception e) {
            return Optional.empty();
        }

        ComponentRelation componentRelation = entity.getBody();

        return Optional.of(componentRelation);
    }


    public void close() {

    }


    public boolean setConfig(C config) {
        if (validConfig(config) && internalConfigSet(config)) {
            logger.info("成功进行组件设置 {},{}", config.getClass().getSimpleName(), componentRelation.getHost());
            return true;
        } else {
            logger.warn("[警告],组件设置失败 {},{}", config.getClass().getSimpleName(), componentRelation.getHost());
            return false;
        }
    }

    //推送任务相关的操作
    public boolean pushTask(T taskOperation) {
        executeTaskOperation(taskOperation);
        return true;
    }

    //执行任务操作 //todo 数据反馈
    private void executeTaskOperation(T taskOperation) {
        if (status().isPresent() && (status().get().getStatus() == StatusEnum.STARTED
                || status().get().getStatus() == StatusEnum.INIT)) {
            OPStatus status = taskOperationRequest(taskOperation);
            if (status == OPStatus.SUCCEED) {
                logger.info("组件添加一个任务{},{}\n{}", componentRelation.getHost(), taskOperation.getClass().getSimpleName(), taskOperation);
            } else if (status == OPStatus.FAILD) {
                logger.info("组件添加一个任务{},{}\n{}", componentRelation.getHost(), taskOperation.getClass().getSimpleName(), taskOperation);
            } else {
                logger.info("任务已经被添加{},{}\n{}", componentRelation.getHost(), taskOperation.getClass().getSimpleName(), taskOperation);
            }
        } else {
            logger.warn("[警告]组件状态异常,无法添加任务,{},{}", taskOperation.getClass().getSimpleName(), componentRelation.getHost());
        }

    }

    //rpc执行任务操作，返回结果
    private OPStatus taskOperationRequest(T taskOperation) {
        OPStatus result = OPStatus.FAILD;
        try {
            result = internalTask(taskOperation);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warn("添加组件失败，进程被打断,{},{}", componentRelation.getHost(), taskOperation.getClass().getSimpleName());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }


    protected abstract OPStatus internalTask(T taskOperation) throws InterruptedException, ExecutionException;

    protected abstract boolean internalConfigSet(C config);

    protected abstract boolean validConfig(C config);


    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ComponentRelation getComponentRelation() {
        return componentRelation;
    }

    public void setComponentRelation(ComponentRelation componentRelation) {
        this.componentRelation = componentRelation;
    }


    public enum OPStatus {
        SUCCEED,
        FAILD
    }
}
