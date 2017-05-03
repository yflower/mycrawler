package com.cufe.taskRpc.rpc;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.server.AbstractComponentHeartServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.proto.status.HeartMessage;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.Status;
import io.grpc.stub.StreamObserver;

/**
 * Created by jal on 2017/4/24.
 */
public class RpcHeartServer extends RpcComponentHeartServiceGrpc.RpcComponentHeartServiceImplBase {

    public RpcHeartServer(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    private ComponentContext componentContext;


    @Override
    public void rpcComponentHeart(HeartMessage request, StreamObserver<HeartMessage> responseObserver) {
        RpcHeartService heartService = new RpcHeartService(componentContext);
        HeartMessage heart = heartService.heart(request);
        responseObserver.onNext(heart);
        responseObserver.onCompleted();
    }

    private class RpcHeartService extends AbstractComponentHeartServer<HeartMessage, HeartMessage> {
        public RpcHeartService(ComponentContext resolveContext) {
            this.componentContext = resolveContext;
        }

        @Override
        protected StatusEnum rpcResToLocal(HeartMessage rpcRes) {
            return StatusEnum.numberOf(rpcRes.getStatusValue());
        }

        @Override
        protected HeartMessage localToRPC_Q(StatusEnum result) {
            return HeartMessage.newBuilder().setStatus(Status.forNumber(result.getCode())).build();
        }
    }
}
