package com.jal.crawler.web.convert;

import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.jal.crawler.web.param.DownloadConfigParam;
import com.jal.crawler.web.param.DownloadTaskOpParam;
import com.jal.crawler.web.param.rpc.DownloadRpcConfigParam;
import com.jal.crawler.web.param.rpc.DownloadRpcTaskOpParam;

/**
 * R
 * Created by jianganlan on 2017/4/23.
 */
public class WebParamToRpcParam {
    public static DownloadRpcConfigParam configConvert(DownloadConfigParam configParam) {
        DownloadRpcConfigParam rpcConfigParam = new DownloadRpcConfigParam();
        rpcConfigParam.setHost(configParam.getHost());
        rpcConfigParam.setPort(configParam.getPort());
        rpcConfigParam.setHttpPort(configParam.getHttpPort());
        rpcConfigParam.setRelationType(configParam.getRelationType());
        if (ComponentRelationTypeEnum.numberOf(configParam.getRelationType()) == ComponentRelationTypeEnum.LEADER) {
            rpcConfigParam.setLeaderHost(configParam.getHost());
            rpcConfigParam.setLeaderPort(configParam.getPort());
        }
        rpcConfigParam.setMongoConfig(configParam.getMongoConfig());
        rpcConfigParam.setRedisConfig(configParam.getRedisConfig());
        rpcConfigParam.setProxy(configParam.isProxy());
        rpcConfigParam.setThread(configParam.getThread());
        rpcConfigParam.setProxyAddress(configParam.getProxyAddress());
        rpcConfigParam.setSleepTime(configParam.getSleepTime());

        return rpcConfigParam;
    }


    public static DownloadRpcTaskOpParam taskOpConvert(DownloadTaskOpParam taskOpParam) {
        DownloadRpcTaskOpParam rpcTaskOpParam = new DownloadRpcTaskOpParam();
        rpcTaskOpParam.setTaskTag(taskOpParam.getTaskTag());
        rpcTaskOpParam.setTaskType(taskOpParam.getTaskType());
        rpcTaskOpParam.setDynamic(taskOpParam.isDynamic());
        rpcTaskOpParam.setPostProcess(taskOpParam.getPostProcess());
        rpcTaskOpParam.setPreProcess(taskOpParam.getPreProcess());
        rpcTaskOpParam.setTest(taskOpParam.isTest());
        rpcTaskOpParam.setUrls(taskOpParam.getUrls());

        return rpcTaskOpParam;
    }
}
