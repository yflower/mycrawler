package com.jal.crawler.proto.resolve;

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
    comments = "Source: resolveConfig.proto")
public class RpcResolveConfigGrpc {

  private RpcResolveConfigGrpc() {}

  public static final String SERVICE_NAME = "grpc.RpcResolveConfig";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.resolve.ResolveConfig,
      com.jal.crawler.proto.config.ConfigStatus> METHOD_RESOLVE_CONFIG =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "grpc.RpcResolveConfig", "resolveConfig"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.resolve.ResolveConfig.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.config.ConfigStatus.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.configComponnet.ConfigComponentStatus,
      com.jal.crawler.proto.resolve.ResolveConfig> METHOD_RESOLVE_CONFIG_SHOW =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "grpc.RpcResolveConfig", "resolveConfigShow"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.configComponnet.ConfigComponentStatus.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.resolve.ResolveConfig.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcResolveConfigStub newStub(io.grpc.Channel channel) {
    return new RpcResolveConfigStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcResolveConfigBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcResolveConfigBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RpcResolveConfigFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcResolveConfigFutureStub(channel);
  }

  /**
   */
  public static abstract class RpcResolveConfigImplBase implements io.grpc.BindableService {

    /**
     */
    public void resolveConfig(com.jal.crawler.proto.resolve.ResolveConfig request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.config.ConfigStatus> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RESOLVE_CONFIG, responseObserver);
    }

    /**
     */
    public void resolveConfigShow(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.resolve.ResolveConfig> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RESOLVE_CONFIG_SHOW, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_RESOLVE_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.resolve.ResolveConfig,
                com.jal.crawler.proto.config.ConfigStatus>(
                  this, METHODID_RESOLVE_CONFIG)))
          .addMethod(
            METHOD_RESOLVE_CONFIG_SHOW,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.configComponnet.ConfigComponentStatus,
                com.jal.crawler.proto.resolve.ResolveConfig>(
                  this, METHODID_RESOLVE_CONFIG_SHOW)))
          .build();
    }
  }

  /**
   */
  public static final class RpcResolveConfigStub extends io.grpc.stub.AbstractStub<RpcResolveConfigStub> {
    private RpcResolveConfigStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveConfigStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveConfigStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveConfigStub(channel, callOptions);
    }

    /**
     */
    public void resolveConfig(com.jal.crawler.proto.resolve.ResolveConfig request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.config.ConfigStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RESOLVE_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void resolveConfigShow(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.resolve.ResolveConfig> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RESOLVE_CONFIG_SHOW, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RpcResolveConfigBlockingStub extends io.grpc.stub.AbstractStub<RpcResolveConfigBlockingStub> {
    private RpcResolveConfigBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveConfigBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveConfigBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveConfigBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.jal.crawler.proto.config.ConfigStatus resolveConfig(com.jal.crawler.proto.resolve.ResolveConfig request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RESOLVE_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.jal.crawler.proto.resolve.ResolveConfig resolveConfigShow(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RESOLVE_CONFIG_SHOW, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RpcResolveConfigFutureStub extends io.grpc.stub.AbstractStub<RpcResolveConfigFutureStub> {
    private RpcResolveConfigFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveConfigFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveConfigFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveConfigFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.config.ConfigStatus> resolveConfig(
        com.jal.crawler.proto.resolve.ResolveConfig request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RESOLVE_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.resolve.ResolveConfig> resolveConfigShow(
        com.jal.crawler.proto.configComponnet.ConfigComponentStatus request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RESOLVE_CONFIG_SHOW, getCallOptions()), request);
    }
  }

  private static final int METHODID_RESOLVE_CONFIG = 0;
  private static final int METHODID_RESOLVE_CONFIG_SHOW = 1;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcResolveConfigImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RpcResolveConfigImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESOLVE_CONFIG:
          serviceImpl.resolveConfig((com.jal.crawler.proto.resolve.ResolveConfig) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.config.ConfigStatus>) responseObserver);
          break;
        case METHODID_RESOLVE_CONFIG_SHOW:
          serviceImpl.resolveConfigShow((com.jal.crawler.proto.configComponnet.ConfigComponentStatus) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.resolve.ResolveConfig>) responseObserver);
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
        METHOD_RESOLVE_CONFIG,
        METHOD_RESOLVE_CONFIG_SHOW);
  }

}
