package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.MongoConfig;
import com.jal.crawler.proto.data.DataConfig;
import com.jal.crawler.proto.data.RpcDataConfigGrpc;
import com.jal.crawler.proto.status.Status;
import com.jal.crawler.web.param.rpc.DataConfigRpcParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class DataInitClient extends AbstractComponentInitClient<ConfigStatus, DataConfig> {
    private RpcDataConfigGrpc.RpcDataConfigBlockingStub stub;

    @Override
    protected <P> DataConfig localReqToRpcReq(P params) {
        DataConfigRpcParam rpcParam = (DataConfigRpcParam) params;
        return DataConfig.newBuilder()
                .setSelfHost(rpcParam.getHost())
                .setSelfPort(rpcParam.getPort())
                .setRelationType(rpcParam.getRelationType())
                .setLeaderHost(rpcParam.getLeaderHost())
                .setLeaderPort(rpcParam.getLeaderPort())
                .setSelfStatus(Status.forNumber(rpcParam.getSelfStatus()))
                .setLeaderStatus(Status.forNumber(rpcParam.getLeaderStatus()))
                .setThread(rpcParam.getThread())
                .setMongoConfig(
                        MongoConfig.newBuilder()
                                .setHost(rpcParam.getMongoConfig().getHost())
                                .setPort(rpcParam.getMongoConfig().getPort())
                                .setDatabase(rpcParam.getMongoConfig().getDatabase())
                                .setUser(rpcParam.getMongoConfig().getUser())
                                .setPassword(rpcParam.getMongoConfig().getPassword())
                                .build()
                )
                .build();
    }

    @Override
    protected boolean rpcResToLocalRes(ConfigStatus rpcRes) {
        return rpcRes.getInit();
    }

    @Override
    protected ConfigStatus rpcSend(DataConfig rpcReq) {
        return stub.dataConfig(rpcReq);
    }

    public void setStub(RpcDataConfigGrpc.RpcDataConfigBlockingStub stub) {
        this.stub = stub;
    }
}
