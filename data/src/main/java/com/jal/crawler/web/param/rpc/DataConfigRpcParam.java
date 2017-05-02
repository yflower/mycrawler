package com.jal.crawler.web.param.rpc;

import com.cufe.taskProcessor.ComponentFacade;
import com.jal.crawler.web.param.MongoConfigParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class DataConfigRpcParam extends ComponentFacade.initParam {
    private MongoConfigParam mongoConfig;


    public MongoConfigParam getMongoConfig() {
        return mongoConfig;
    }

    public void setMongoConfig(MongoConfigParam mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

}
