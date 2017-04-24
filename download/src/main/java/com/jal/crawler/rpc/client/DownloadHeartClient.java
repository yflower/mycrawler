package com.jal.crawler.rpc.client;

import com.cufe.taskProcessor.rpc.client.AbstractComponentHeartClient;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.proto.status.HeartMessage;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.Status;

/**
 * Created by jal on 2017/4/24.
 */
public class DownloadHeartClient extends AbstractComponentHeartClient<HeartMessage, HeartMessage> {
    private RpcComponentHeartServiceGrpc.RpcComponentHeartServiceBlockingStub stub;

    @Override
    protected HeartMessage localReqToRpcReq(StatusEnum fromStatus) {
        return HeartMessage.newBuilder().setStatus(Status.forNumber(fromStatus.getCode())).build();
    }

    @Override
    protected StatusEnum rpcResToLocalRes(HeartMessage rpcRes) {
        return StatusEnum.numberOf(rpcRes.getStatusValue());
    }

    @Override
    protected HeartMessage rpcSend(HeartMessage rpcReq) {
        return stub.rpcComponentHeart(rpcReq);
    }

    public void setStub(RpcComponentHeartServiceGrpc.RpcComponentHeartServiceBlockingStub stub) {
        this.stub = stub;
    }
}
