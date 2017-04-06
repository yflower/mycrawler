package com.jal.crawler.context;

import com.jal.crawler.rpc.RpcClient;
import com.jal.crawler.web.data.model.component.ComponentModel;
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
    private RpcClient rpcClient;

    private MongoConfigModel mongoConfigModel;

    private RedisConfigModel redisConfigModel;

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public List<ComponentModel> resolveComponent() {
        return rpcClient.resolveModel();
    }

    public List<ComponentModel> downloadComponent() {
        return rpcClient.downloadModel();
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
