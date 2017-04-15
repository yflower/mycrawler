package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
import com.jal.crawler.web.param.DownloadConfigParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadInitClient extends AbstractComponentInitClient<ConfigStatus, DownloadConfig> {

    private RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub stub;

    @Override
    protected <P> DownloadConfig localReqToRpcReq(int thread, P params) {
        DownloadConfigParam param= (DownloadConfigParam) params;
        return DownloadConfig.newBuilder()
                .setThread(thread)
                .setSleepTime(param.getSleepTime())
                .setProxy(param.isProxy())
                .addAllProxyAddress(param.getProxyAddress() == null ? new ArrayList<String>() : param.getProxyAddress())
                .setPersist(DownloadConfig.Persist.REDIS)
                .setRedisConfig(
                        RedisConfig.newBuilder()
                                .setHost(param.getRedisConfigParam().getHost())
                                .setPort(param.getRedisConfigParam().getPort())
                                .setPassword(param.getRedisConfigParam().getPassword())
                                .build()
                )
                .build();
    }

    @Override
    protected boolean rpcResToLocalRes(ConfigStatus rpcRes) {
        return rpcRes.getInit();
    }

    @Override
    protected ConfigStatus rpcSend(DownloadConfig rpcReq) {
        return stub.downloadConfig(rpcReq);
    }

    public void setStub(RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub stub) {
        this.stub = stub;
    }
}
