package com.jal.crawler.rpc;

import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.rpc.server.AbstractComponentHeartServer;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.proto.status.HeartMessage;
import com.jal.crawler.proto.status.RpcComponentHeartServiceGrpc;
import com.jal.crawler.proto.status.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by jal on 2017/4/24.
 */
@Component
public class DownloadHeartServer extends RpcComponentHeartServiceGrpc.RpcComponentHeartServiceImplBase {

    @Resource
    private ComponentContext componentContext;


    @Override
    public void rpcComponentHeart(HeartMessage request, StreamObserver<HeartMessage> responseObserver) {
        DownloadHeartService heartService = new DownloadHeartService(componentContext);
        HeartMessage heart = heartService.heart(request);
        responseObserver.onNext(heart);
        responseObserver.onCompleted();
    }

    private class DownloadHeartService extends AbstractComponentHeartServer< HeartMessage, HeartMessage> {
        public DownloadHeartService(ComponentContext componentContext) {
            this.componentContext = componentContext;
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
