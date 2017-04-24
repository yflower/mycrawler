package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.proto.resolve.RpcResolveConfigGrpc;
import com.jal.crawler.web.param.rpc.ResolveConfigRpcParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveInitClient extends AbstractComponentInitClient<ConfigStatus, ResolveConfig> {
    private RpcResolveConfigGrpc.RpcResolveConfigBlockingStub stub;

    @Override
    protected <P> ResolveConfig localReqToRpcReq(P params) {
        ResolveConfigRpcParam rpcParam = (ResolveConfigRpcParam) params;
        return ResolveConfig.newBuilder()
                .setPersist(ResolveConfig.Persist.MONGO)
                .setThread(rpcParam.getThread())
                .setMongoConfig(
                        ResolveConfig.MongoConfig.newBuilder()
                                .setHost(rpcParam.getMongoConfig().getHost())
                                .setPort(rpcParam.getMongoConfig().getPort())
                                .setDatabase(rpcParam.getMongoConfig().getDatabase())
                                .setUser(rpcParam.getMongoConfig().getUser())
                                .setPassword(rpcParam.getMongoConfig().getPassword())
                                .build()
                )
                .setRedisConfig(
                        RedisConfig.newBuilder()
                                .setHost(rpcParam.getRedisConfig().getHost())
                                .setPort(rpcParam.getRedisConfig().getPort())
                                .setPassword(rpcParam.getRedisConfig().getPassword())
                                .build())
                .build();
    }

    @Override
    protected boolean rpcResToLocalRes(ConfigStatus rpcRes) {
        return rpcRes.getInit();
    }

    @Override
    protected ConfigStatus rpcSend(ResolveConfig rpcReq) {
        return stub.resolveConfig(rpcReq);
    }

    public void setStub(RpcResolveConfigGrpc.RpcResolveConfigBlockingStub stub) {
        this.stub = stub;
    }
}
