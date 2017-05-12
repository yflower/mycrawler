package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.proto.download.RpcDownlandConfigGrpc;
import com.jal.crawler.proto.status.Status;
import com.jal.crawler.web.param.rpc.DownloadRpcConfigParam;

import java.util.ArrayList;

/**
 * Created by jianganlan on 2017/4/15.
 */
public class DownloadInitClient extends AbstractComponentInitClient<ConfigStatus, DownloadConfig> {

    private RpcDownlandConfigGrpc.RpcDownlandConfigBlockingStub stub;

    @Override
    protected <P> DownloadConfig localReqToRpcReq(P params) {
        DownloadRpcConfigParam param = (DownloadRpcConfigParam) params;
        return DownloadConfig.newBuilder()
                .setSelfHost(param.getHost())
                .setSelfPort(param.getPort())
                .setSelfHttpPort(param.getHttpPort())
                .setRelationType(param.getRelationType())
                .setLeaderHost(param.getLeaderHost())
                .setLeaderPort(param.getLeaderPort())
                .setLeaderHttpPort(param.getLeaderHttpPort())
                .setSelfStatus(Status.forNumber(param.getSelfStatus()))
                .setLeaderStatus(Status.forNumber(param.getLeaderStatus()))
                .setThread(param.getThread())
                .setSleepTime(param.getSleepTime())
                .setProxy(param.isProxy())
                .addAllProxyAddress(param.getProxyAddress() == null ? new ArrayList<String>() : param.getProxyAddress())
                .setPersist(DownloadConfig.Persist.REDIS)
                .setRedisConfig(
                        RedisConfig.newBuilder()
                                .setHost(param.getRedisConfig().getHost())
                                .setPort(param.getRedisConfig().getPort())
                                .setPassword(param.getRedisConfig().getPassword())
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
