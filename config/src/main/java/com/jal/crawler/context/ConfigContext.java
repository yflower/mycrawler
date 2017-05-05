package com.jal.crawler.context;

import com.jal.crawler.rpc.HttpClientHolder;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.data.model.dbModel.MongoConfigModel;
import com.jal.crawler.web.data.model.dbModel.RedisConfigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 整个config的context
 * Created by jal on 2017/2/19.
 */
@Component
public class ConfigContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigContext.class);

    @Resource
    private HttpClientHolder rpcClient;

    private MongoConfigModel mongoConfigModel;

    private RedisConfigModel redisConfigModel;

    public HttpClientHolder getRpcClient() {
        return rpcClient;
    }

    public List<ComponentRelation> resolveComponent() {
        return rpcClient.resolveModel();
    }

    public List<ComponentRelation> downloadComponent() {
        return rpcClient.downloadModel();
    }

    public List<ComponentRelation> dataComponent(){return rpcClient.dataModel();}

    public List<ComponentRelation> linkComponent(){
        return rpcClient.linkModel();
    }

    public MongoConfigModel getMongoConfigModel() {
        return mongoConfigModel;
    }

    public void setMongoConfigModel(MongoConfigModel mongoConfigModel) {
        this.mongoConfigModel = mongoConfigModel;
    }

    public RedisConfigModel getRedisConfigModel() {
        return redisConfigModel;
    }

    public void setRedisConfigModel(RedisConfigModel redisConfigModel) {
        this.redisConfigModel = redisConfigModel;
    }


}
