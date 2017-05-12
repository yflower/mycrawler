package com.jal.crawler.web.convert;

import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.jal.crawler.web.param.ResolveConfigParam;
import com.jal.crawler.web.param.ResolveTaskOpParam;
import com.jal.crawler.web.param.rpc.ResolveConfigRpcParam;
import com.jal.crawler.web.param.rpc.ResolveTaskOpRpcParam;

/**
 * R
 * Created by jianganlan on 2017/4/23.
 */
public class WebParamToRpcParam {
    public static ResolveConfigRpcParam configConvert(ResolveConfigParam configParam) {
        ResolveConfigRpcParam rpcConfigParam = new ResolveConfigRpcParam();
        rpcConfigParam.setHost(configParam.getHost());
        rpcConfigParam.setPort(configParam.getPort());
        rpcConfigParam.setRelationType(configParam.getRelationType());
        rpcConfigParam.setHttpPort(configParam.getHttpPort());
        if (ComponentRelationTypeEnum.numberOf(configParam.getRelationType()) == ComponentRelationTypeEnum.LEADER) {
            rpcConfigParam.setLeaderHost(configParam.getHost());
            rpcConfigParam.setLeaderPort(configParam.getPort());
        }
        rpcConfigParam.setMongoConfig(configParam.getMongoConfig());
        rpcConfigParam.setRedisConfig(configParam.getRedisConfig());
        rpcConfigParam.setThread(configParam.getThread());

        return rpcConfigParam;
    }


    public static ResolveTaskOpRpcParam taskOpConvert(ResolveTaskOpParam taskOpParam) {
        ResolveTaskOpRpcParam rpcTaskOpParam = new ResolveTaskOpRpcParam();
        rpcTaskOpParam.setTaskTag(taskOpParam.getTaskTag());
        rpcTaskOpParam.setTaskType(taskOpParam.getTaskType());
        rpcTaskOpParam.setTest(taskOpParam.isTest());
        rpcTaskOpParam.setVars(taskOpParam.getVars());
        rpcTaskOpParam.setItems(taskOpParam.getItems());
        return rpcTaskOpParam;
    }
}
