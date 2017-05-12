package com.jal.crawler.http;

import com.jal.crawler.web.data.model.component.DownloadConfigRelation;
import com.jal.crawler.web.data.model.task.DownloadOperationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by jianganlan on 2017/4/26.
 */
public class DownloadHttpClient extends AbstractHttpClient<DownloadConfigRelation, DownloadOperationModel> {


    @Override
    public Optional<ResponseEntity> result(Map<String, Object> param) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected OPStatus internalTask(DownloadOperationModel taskOperation) throws InterruptedException, ExecutionException {
        String url = "http://" + this.componentRelation.getHost() + ":"+port()+"/download/task";
        Map<String, Object> body = new HashMap();
        body.put("taskTag", taskOperation.getTaskTag());
        body.put("taskType", taskOperation.getTaskType().getCode());
        body.put("test", taskOperation.isTest());
        body.put("dynamic", taskOperation.isDynamic());
        body.put("urls",taskOperation.getUrls());
        body.put("preProcess", taskOperation.getPreProcess());
        body.put("postProcess", taskOperation.getPostProcess());

        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);


        return entity.getStatusCode()==HttpStatus.OK?OPStatus.SUCCEED:OPStatus.FAILED;
    }

    @Override
    protected boolean internalConfigSet(DownloadConfigRelation config) {

        String url = "http://" + this.componentRelation.getHost() + ":"+port()+"/download/init";
        Map<String, Object> body = new HashMap();
        body.put("host", config.getHost());
        body.put("port", config.getPort());
        body.put("httpPort",config.getServerPort());
        body.put("thread", config.getThread());
        body.put("relationType", config.getRelationTypeEnum().getCode());
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

    @Override
    protected int port() {
        return componentRelation.getServerPort();
    }

}
