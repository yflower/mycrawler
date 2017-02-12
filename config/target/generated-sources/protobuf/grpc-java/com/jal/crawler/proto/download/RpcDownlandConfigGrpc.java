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
    comments = "Source: downloadConfig.proto")
public class RpcDownlandConfigGrpc {

  private RpcDownlandConfigGrpc() {}

  public static final String SERVICE_NAME = "grpc.RpcDownlandConfig";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.download.DownloadConfig,
      com.jal.crawler.proto.config.ConfigStatus> METHOD_DOWNLOAD_CONFIG =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "grpc.RpcDownlandConfig", "downloadConfig"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.download.DownloadConfig.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.config.ConfigStatus.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.configComponnet.ConfigComponentStatus,
      com.jal.crawler.proto.download.DownloadConfig> METHOD_DOWNLOAD_CONFIG_SHOW =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "grpc.RpcDownlandConfig", "downloadConfigShow"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.configComponnet.ConfigComponentStatus.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.download.DownloadConfig.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcDownlandConfigStub newStub(io.grpc.Channel channel) {
    return new RpcDownlandConfigStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcDownlandConfigBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcDownlandConfigBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RpcDownlandConfigFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcDownlandConfigFutureStub(channel);
  }

  /**
   */
  public static abstract class RpcDownlandConfigImplBase implements io.grpc.BindableService {

    /**
     */
    public void downloadConfig(com.jal.crawler.proto.download.DownloadConfig request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.config.ConfigStatus> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DOWNLOAD_CONFIG, responseObserver);
    }

    /**
     */
    public void downloadConfigShow(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.download.DownloadConfig> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DOWNLOAD_CONFIG_SHOW, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_DOWNLOAD_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.download.DownloadConfig,
                com.jal.crawler.proto.config.ConfigStatus>(
                  this, METHODID_DOWNLOAD_CONFIG)))
          .addMethod(
            METHOD_DOWNLOAD_CONFIG_SHOW,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.configComponnet.ConfigComponentStatus,
                com.jal.crawler.proto.download.DownloadConfig>(
                  this, METHODID_DOWNLOAD_CONFIG_SHOW)))
          .build();
    }
  }

  /**
   */
  public static final class RpcDownlandConfigStub extends io.grpc.stub.AbstractStub<RpcDownlandConfigStub> {
    private RpcDownlandConfigStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcDownlandConfigStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcDownlandConfigStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcDownlandConfigStub(channel, callOptions);
    }

    /**
     */
    public void downloadConfig(com.jal.crawler.proto.download.DownloadConfig request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.config.ConfigStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DOWNLOAD_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void downloadConfigShow(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.download.DownloadConfig> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DOWNLOAD_CONFIG_SHOW, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RpcDownlandConfigBlockingStub extends io.grpc.stub.AbstractStub<RpcDownlandConfigBlockingStub> {
    private RpcDownlandConfigBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcDownlandConfigBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcDownlandConfigBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcDownlandConfigBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.jal.crawler.proto.config.ConfigStatus downloadConfig(com.jal.crawler.proto.download.DownloadConfig request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DOWNLOAD_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.jal.crawler.proto.download.DownloadConfig downloadConfigShow(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DOWNLOAD_CONFIG_SHOW, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RpcDownlandConfigFutureStub extends io.grpc.stub.AbstractStub<RpcDownlandConfigFutureStub> {
    private RpcDownlandConfigFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcDownlandConfigFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcDownlandConfigFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcDownlandConfigFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.config.ConfigStatus> downloadConfig(
        com.jal.crawler.proto.download.DownloadConfig request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DOWNLOAD_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.download.DownloadConfig> downloadConfigShow(
        com.jal.crawler.proto.configComponnet.ConfigComponentStatus request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DOWNLOAD_CONFIG_SHOW, getCallOptions()), request);
    }
  }

  private static final int METHODID_DOWNLOAD_CONFIG = 0;
  private static final int METHODID_DOWNLOAD_CONFIG_SHOW = 1;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcDownlandConfigImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RpcDownlandConfigImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DOWNLOAD_CONFIG:
          serviceImpl.downloadConfig((com.jal.crawler.proto.download.DownloadConfig) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.config.ConfigStatus>) responseObserver);
          break;
        case METHODID_DOWNLOAD_CONFIG_SHOW:
          serviceImpl.downloadConfigShow((com.jal.crawler.proto.configComponnet.ConfigComponentStatus) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.download.DownloadConfig>) responseObserver);
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
        METHOD_DOWNLOAD_CONFIG,
        METHOD_DOWNLOAD_CONFIG_SHOW);
  }

}
