package com.jal.crawler.web.convert;

import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.jal.crawler.web.param.DataConfigParam;
import com.jal.crawler.web.param.DataTaskOpParam;
import com.jal.crawler.web.param.rpc.DataConfigRpcParam;
import com.jal.crawler.web.param.rpc.DataTaskOpRpcParam;

/**
 * R
 * Created by jianganlan on 2017/4/23.
 */
public class WebParamToRpcParam {
    public static DataConfigRpcParam configConvert(DataConfigParam configParam) {
        DataConfigRpcParam rpcConfigParam = new DataConfigRpcParam();
        rpcConfigParam.setHost(configParam.getHost());
        rpcConfigParam.setPort(configParam.getPort());
        rpcConfigParam.setRelationType(configParam.getRelationType());
        if (ComponentRelationTypeEnum.numberOf(configParam.getRelationType()) == ComponentRelationTypeEnum.LEADER) {
            rpcConfigParam.setLeaderHost(configParam.getHost());
            rpcConfigParam.setLeaderPort(configParam.getPort());
        }
        rpcConfigParam.setMongoConfig(configParam.getMongoConfig());
        rpcConfigParam.setThread(configParam.getThread());

        return rpcConfigParam;
    }


    public static DataTaskOpRpcParam taskOpConvert(DataTaskOpParam taskOpParam) {
        DataTaskOpRpcParam rpcTaskOpParam = new DataTaskOpRpcParam();
        rpcTaskOpParam.setTaskTag(taskOpParam.getTaskTag());
        rpcTaskOpParam.setTaskType(taskOpParam.getTaskType());
        rpcTaskOpParam.setTest(taskOpParam.isTest());
        rpcTaskOpParam.setDataType(taskOpParam.getDataType());
        return rpcTaskOpParam;
    }
}
