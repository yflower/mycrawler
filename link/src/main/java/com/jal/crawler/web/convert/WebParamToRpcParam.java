package com.jal.crawler.web.convert;

import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.jal.crawler.web.param.LinkConfigParam;
import com.jal.crawler.web.param.LinkTaskOpParam;
import com.jal.crawler.web.param.rpc.LinkConfigRpcParam;
import com.jal.crawler.web.param.rpc.LinkTaskOpRpcParam;

/**
 * R
 * Created by jianganlan on 2017/4/23.
 */
public class WebParamToRpcParam {
    public static LinkConfigRpcParam configConvert(LinkConfigParam configParam) {
        LinkConfigRpcParam rpcConfigParam = new LinkConfigRpcParam();
        rpcConfigParam.setHost(configParam.getHost());
        rpcConfigParam.setPort(configParam.getPort());
        rpcConfigParam.setRelationType(configParam.getRelationType());
        if (ComponentRelationTypeEnum.numberOf(configParam.getRelationType()) == ComponentRelationTypeEnum.LEADER) {
            rpcConfigParam.setLeaderHost(configParam.getHost());
            rpcConfigParam.setLeaderPort(configParam.getPort());
        }
        rpcConfigParam.setMongoConfig(configParam.getMongoConfig());
        rpcConfigParam.setThread(configParam.getThread());
        rpcConfigParam.setRedisConfig(configParam.getRedisConfig());

        return rpcConfigParam;
    }


    public static LinkTaskOpRpcParam taskOpConvert(LinkTaskOpParam taskOpParam) {
        LinkTaskOpRpcParam rpcTaskOpParam = new LinkTaskOpRpcParam();
        rpcTaskOpParam.setTaskTag(taskOpParam.getTaskTag());
        rpcTaskOpParam.setTaskType(taskOpParam.getTaskType());
        rpcTaskOpParam.setTest(taskOpParam.isTest());
        rpcTaskOpParam.setLinkPattern(taskOpParam.getLinkPattern());
        return rpcTaskOpParam;
    }
}
