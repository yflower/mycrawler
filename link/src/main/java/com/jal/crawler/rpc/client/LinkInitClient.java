package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentInitClient;
import com.jal.crawler.proto.config.ConfigStatus;
import com.jal.crawler.proto.config.MongoConfig;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.link.LinkConfig;
import com.jal.crawler.proto.link.RpcLinkConfigGrpc;
import com.jal.crawler.proto.status.Status;
import com.jal.crawler.web.param.rpc.LinkConfigRpcParam;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class LinkInitClient extends AbstractComponentInitClient<ConfigStatus, LinkConfig> {
    private RpcLinkConfigGrpc.RpcLinkConfigBlockingStub stub;

    @Override
    protected <P> LinkConfig localReqToRpcReq(P params) {
        LinkConfigRpcParam rpcParam = (LinkConfigRpcParam) params;
        return LinkConfig.newBuilder()
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
    protected ConfigStatus rpcSend(LinkConfig rpcReq) {
        return stub.linkConfig(rpcReq);
    }

    public void setStub(RpcLinkConfigGrpc.RpcLinkConfigBlockingStub stub) {
        this.stub = stub;
    }
}
