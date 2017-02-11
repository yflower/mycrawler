package com.jal.crawler.proto.download;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.3)",
    comments = "Source: downloadTask.proto")
public class RpcDownloadTaskGrpc {

  private RpcDownloadTaskGrpc() {}

  public static final String SERVICE_NAME = "rpc.RpcDownloadTask";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.download.DownloadTask,
      com.jal.crawler.proto.download.DownloadTaskResponse> METHOD_DOWNLOAD_TASK =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "rpc.RpcDownloadTask", "downloadTask"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.download.DownloadTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.download.DownloadTaskResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcDownloadTaskStub newStub(io.grpc.Channel channel) {
    return new RpcDownloadTaskStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcDownloadTaskBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcDownloadTaskBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RpcDownloadTaskFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcDownloadTaskFutureStub(channel);
  }

  /**
   */
  public static abstract class RpcDownloadTaskImplBase implements io.grpc.BindableService {

    /**
     */
    public void downloadTask(com.jal.crawler.proto.download.DownloadTask request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.download.DownloadTaskResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DOWNLOAD_TASK, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_DOWNLOAD_TASK,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.download.DownloadTask,
                com.jal.crawler.proto.download.DownloadTaskResponse>(
                  this, METHODID_DOWNLOAD_TASK)))
          .build();
    }
  }

  /**
   */
  public static final class RpcDownloadTaskStub extends io.grpc.stub.AbstractStub<RpcDownloadTaskStub> {
    private RpcDownloadTaskStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcDownloadTaskStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcDownloadTaskStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcDownloadTaskStub(channel, callOptions);
    }

    /**
     */
    public void downloadTask(com.jal.crawler.proto.download.DownloadTask request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.download.DownloadTaskResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DOWNLOAD_TASK, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RpcDownloadTaskBlockingStub extends io.grpc.stub.AbstractStub<RpcDownloadTaskBlockingStub> {
    private RpcDownloadTaskBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcDownloadTaskBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcDownloadTaskBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcDownloadTaskBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.jal.crawler.proto.download.DownloadTaskResponse downloadTask(com.jal.crawler.proto.download.DownloadTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DOWNLOAD_TASK, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RpcDownloadTaskFutureStub extends io.grpc.stub.AbstractStub<RpcDownloadTaskFutureStub> {
    private RpcDownloadTaskFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcDownloadTaskFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcDownloadTaskFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcDownloadTaskFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.download.DownloadTaskResponse> downloadTask(
        com.jal.crawler.proto.download.DownloadTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DOWNLOAD_TASK, getCallOptions()), request);
    }
  }

  private static final int METHODID_DOWNLOAD_TASK = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcDownloadTaskImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RpcDownloadTaskImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DOWNLOAD_TASK:
          serviceImpl.downloadTask((com.jal.crawler.proto.download.DownloadTask) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.download.DownloadTaskResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_DOWNLOAD_TASK);
  }

}
