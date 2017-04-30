package com.jal.crawler.rpc;

import com.jal.crawler.web.data.enums.ComponentRelationTypeEnum;
import com.jal.crawler.web.data.model.component.DownloadConfigRelation;
import com.jal.crawler.web.data.model.task.DownloadOperationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.relation.RelationType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by jianganlan on 2017/4/26.
 */
public class DownloadHttpClient extends AbstractHttpClient<DownloadConfigRelation, DownloadOperationModel> {


    @Override
    protected OPStatus internalTask(DownloadOperationModel taskOperation) throws InterruptedException, ExecutionException {
        String url = "http://" + this.componentRelation.getHost() + ":8080/download/task";
        Map<String, Object> body = new HashMap();
        body.put("taskTag", taskOperation.getTaskTag());
        body.put("taskType", taskOperation.getTaskType().getCode());
        body.put("test", taskOperation.isTest());
        body.put("dynamic", taskOperation.isDynamic());
        body.put("preProcess", taskOperation.getPreProcess());
        body.put("postProcess", taskOperation.getPostProcess());

        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);


        return entity.getStatusCode()==HttpStatus.OK?OPStatus.SUCCEED:OPStatus.FAILD;
    }

    @Override
    protected boolean internalConfigSet(DownloadConfigRelation config) {

        String url = "http://" + this.componentRelation.getHost() + ":8080/download/init";
        Map<String, Object> body = new HashMap();
        body.put("host", componentRelation.getHost());
        body.put("port", componentRelation.getPort());
        body.put("thread", config.getThread());
        body.put("relationType", ComponentRelationTypeEnum.LEADER.getCode());
        body.put("sleepTime", config.getSleepTime());
        body.put("proxy", config.isProxy());
        body.put("proxyAddress", config.getProxyAddress());
        body.put("mongoConfig", config.getMongoConfigModel());
        body.put("redisConfig", config.getRedisConfigModel());

        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);

        return entity.getStatusCode() == HttpStatus.OK;
    }

    @Override
    protected boolean validConfig(DownloadConfigRelation config) {
        return true;
    }

}
