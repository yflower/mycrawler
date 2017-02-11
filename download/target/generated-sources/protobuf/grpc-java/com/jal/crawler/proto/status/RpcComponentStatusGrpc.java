package com.jal.crawler.proto.status;

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
    comments = "Source: component.proto")
public class RpcComponentStatusGrpc {

  private RpcComponentStatusGrpc() {}

  public static final String SERVICE_NAME = "RpcComponentStatus";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.configComponnet.ConfigComponentStatus,
      com.jal.crawler.proto.status.ComponentStatus> METHOD_RPC_COMPONENT_STATUS =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "RpcComponentStatus", "rpcComponentStatus"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.configComponnet.ConfigComponentStatus.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.status.ComponentStatus.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcComponentStatusStub newStub(io.grpc.Channel channel) {
    return new RpcComponentStatusStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcComponentStatusBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcComponentStatusBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RpcComponentStatusFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcComponentStatusFutureStub(channel);
  }

  /**
   */
  public static abstract class RpcComponentStatusImplBase implements io.grpc.BindableService {

    /**
     */
    public void rpcComponentStatus(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.status.ComponentStatus> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RPC_COMPONENT_STATUS, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_RPC_COMPONENT_STATUS,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.configComponnet.ConfigComponentStatus,
                com.jal.crawler.proto.status.ComponentStatus>(
                  this, METHODID_RPC_COMPONENT_STATUS)))
          .build();
    }
  }

  /**
   */
  public static final class RpcComponentStatusStub extends io.grpc.stub.AbstractStub<RpcComponentStatusStub> {
    private RpcComponentStatusStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcComponentStatusStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcComponentStatusStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcComponentStatusStub(channel, callOptions);
    }

    /**
     */
    public void rpcComponentStatus(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.status.ComponentStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RPC_COMPONENT_STATUS, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RpcComponentStatusBlockingStub extends io.grpc.stub.AbstractStub<RpcComponentStatusBlockingStub> {
    private RpcComponentStatusBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcComponentStatusBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcComponentStatusBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcComponentStatusBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.jal.crawler.proto.status.ComponentStatus rpcComponentStatus(com.jal.crawler.proto.configComponnet.ConfigComponentStatus request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RPC_COMPONENT_STATUS, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RpcComponentStatusFutureStub extends io.grpc.stub.AbstractStub<RpcComponentStatusFutureStub> {
    private RpcComponentStatusFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcComponentStatusFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcComponentStatusFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcComponentStatusFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.status.ComponentStatus> rpcComponentStatus(
        com.jal.crawler.proto.configComponnet.ConfigComponentStatus request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RPC_COMPONENT_STATUS, getCallOptions()), request);
    }
  }

  private static final int METHODID_RPC_COMPONENT_STATUS = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcComponentStatusImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RpcComponentStatusImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RPC_COMPONENT_STATUS:
          serviceImpl.rpcComponentStatus((com.jal.crawler.proto.configComponnet.ConfigComponentStatus) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.status.ComponentStatus>) responseObserver);
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
        METHOD_RPC_COMPONENT_STATUS);
  }

}
