package com.jal.crawler.http;

import com.jal.crawler.web.data.model.component.DataConfigRelation;
import com.jal.crawler.web.data.model.task.DataOperationModel;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by jianganlan on 2017/5/3.
 */
public class DataHttpClient extends AbstractHttpClient<DataConfigRelation, DataOperationModel> {
    @Override
    public Optional<ResponseEntity> result(Map<String, Object> param) {
        String url = "http://" + this.componentRelation.getHost() + ":" + port() + "/data/result?taskTag={taskTag}&type={type}";
        Map<String, Object> body = new HashMap();
        body.put("taskTag", param.get("taskTag"));
        body.put("type", param.get("type"));

        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        //文件下载特殊处理
        if (((Integer) param.get("type")) != 3) {
            clientHttpRequestFactory.setBufferRequestBody(false);
            restTemplate.setRequestFactory(clientHttpRequestFactory);
            ResponseEntity<Resource> entity= restTemplate.getForEntity(url, Resource.class, body);
            if (entity.getStatusCode() == HttpStatus.OK) {
                return Optional.of(
                        ResponseEntity.ok()
                                .headers(entity.getHeaders())
                                .body(entity.getBody())
                );
            } else {
                return Optional.empty();
            }
        } else {
            clientHttpRequestFactory.setBufferRequestBody(true);
            restTemplate.setRequestFactory(clientHttpRequestFactory);
            ResponseEntity entity = restTemplate.getForEntity(url, String.class, body);
            if (entity.getStatusCode() == HttpStatus.OK) {
                return Optional.of(
                        ResponseEntity.ok()
                                .headers(entity.getHeaders())
                                .body(entity.getBody())
                );
            } else {
                return Optional.empty();
            }
        }



    }

    @Override
    protected OPStatus internalTask(DataOperationModel taskOperation) throws InterruptedException, ExecutionException {
        String url = "http://" + this.componentRelation.getHost() + ":" + port() + "/data/task";
        Map<String, Object> body = new HashMap();
        body.put("taskTag", taskOperation.getTaskTag());
        body.put("taskType", taskOperation.getTaskType().getCode());
        body.put("test", taskOperation.isTest());
        body.put("dataType", taskOperation.getDataTypeEnum().getType());


        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);


        return entity.getStatusCode() == HttpStatus.OK ? OPStatus.SUCCEED : OPStatus.FAILED;
    }

    @Override
    protected boolean internalConfigSet(DataConfigRelation config) {
        String url = "http://" + this.componentRelation.getHost() + ":" + port() + "/data/init";
        Map<String, Object> body = new HashMap();
        body.put("host", config.getHost());
        body.put("port", config.getPort());
        body.put("httpPort",config.getServerPort());
        body.put("thread", config.getThread());
        body.put("relationType", config.getRelationTypeEnum().getCode());
        body.put("mongoConfig", config.getMongoConfigModel());
        ResponseEntity<String> entity = restTemplate.postForEntity(url, body, String.class);
        return entity.getStatusCode() == HttpStatus.OK;
    }

    @Override
    protected boolean validConfig(DataConfigRelation config) {
        return true;
    }

    @Override
    protected int port() {
        return componentRelation.getServerPort();
    }
}
