package com.jal.crawler.http;

import com.jal.crawler.web.data.model.component.ResolveConfigRelation;
import com.jal.crawler.web.data.model.task.ResolveOperationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by jianganlan on 2017/4/26.
 */
public class ResolveHttpClient extends AbstractHttpClient<ResolveConfigRelation, ResolveOperationModel> {


    @Override
    public Optional<ResponseEntity> result(Map<String, Object> param) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected OPStatus internalTask(ResolveOperationModel taskOperation) throws InterruptedException, ExecutionException {
        String url = "http://" + this.componentRelation.getHost() + ":"+port()+"/resolve/task";
        Map<String, Object> body = new HashMap();
        body.put("taskTag", taskOperation.getTaskTag());
        body.put("taskType", taskOperation.getTaskType().getCode());
        body.put("test", taskOperation.isTest());
        body.put("vars",taskOperation.getVars());
        body.put("items",taskOperation.getItems());

        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);


        return entity.getStatusCode()==HttpStatus.OK?OPStatus.SUCCEED:OPStatus.FAILED;
    }

    @Override
    protected boolean internalConfigSet(ResolveConfigRelation config) {
        String url = "http://" + this.componentRelation.getHost() + ":"+port()+"/resolve/init";
        Map<String, Object> body = new HashMap();
        body.put("host", config.getHost());
        body.put("port", config.getPort());
        body.put("thread", config.getThread());
        body.put("httpPort",config.getServerPort());
        body.put("relationType", config.getRelationTypeEnum().getCode());
        body.put("mongoConfig", config.getMongoConfigModel());
        body.put("redisConfig", config.getRedisConfigModel());

        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);

        return entity.getStatusCode() == HttpStatus.OK;
    }

    @Override
    protected boolean validConfig(ResolveConfigRelation config) {
        return true;
    }

    @Override
    protected int port() {
        return componentRelation.getServerPort();
    }
}
