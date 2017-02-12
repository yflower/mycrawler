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
    comments = "Source: resolveTask.proto")
public class RpcResolveTaskGrpc {

  private RpcResolveTaskGrpc() {}

  public static final String SERVICE_NAME = "rpc.RpcResolveTask";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.jal.crawler.proto.resolve.ResolveTask,
      com.jal.crawler.proto.resolve.ResolveTaskResponse> METHOD_RESOLVE_TASK =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "rpc.RpcResolveTask", "resolveTask"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.resolve.ResolveTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.jal.crawler.proto.resolve.ResolveTaskResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcResolveTaskStub newStub(io.grpc.Channel channel) {
    return new RpcResolveTaskStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcResolveTaskBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcResolveTaskBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RpcResolveTaskFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcResolveTaskFutureStub(channel);
  }

  /**
   */
  public static abstract class RpcResolveTaskImplBase implements io.grpc.BindableService {

    /**
     */
    public void resolveTask(com.jal.crawler.proto.resolve.ResolveTask request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.resolve.ResolveTaskResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RESOLVE_TASK, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_RESOLVE_TASK,
            asyncUnaryCall(
              new MethodHandlers<
                com.jal.crawler.proto.resolve.ResolveTask,
                com.jal.crawler.proto.resolve.ResolveTaskResponse>(
                  this, METHODID_RESOLVE_TASK)))
          .build();
    }
  }

  /**
   */
  public static final class RpcResolveTaskStub extends io.grpc.stub.AbstractStub<RpcResolveTaskStub> {
    private RpcResolveTaskStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveTaskStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveTaskStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveTaskStub(channel, callOptions);
    }

    /**
     */
    public void resolveTask(com.jal.crawler.proto.resolve.ResolveTask request,
        io.grpc.stub.StreamObserver<com.jal.crawler.proto.resolve.ResolveTaskResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RESOLVE_TASK, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RpcResolveTaskBlockingStub extends io.grpc.stub.AbstractStub<RpcResolveTaskBlockingStub> {
    private RpcResolveTaskBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveTaskBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveTaskBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveTaskBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.jal.crawler.proto.resolve.ResolveTaskResponse resolveTask(com.jal.crawler.proto.resolve.ResolveTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RESOLVE_TASK, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RpcResolveTaskFutureStub extends io.grpc.stub.AbstractStub<RpcResolveTaskFutureStub> {
    private RpcResolveTaskFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveTaskFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveTaskFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveTaskFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.jal.crawler.proto.resolve.ResolveTaskResponse> resolveTask(
        com.jal.crawler.proto.resolve.ResolveTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RESOLVE_TASK, getCallOptions()), request);
    }
  }

  private static final int METHODID_RESOLVE_TASK = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcResolveTaskImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RpcResolveTaskImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESOLVE_TASK:
          serviceImpl.resolveTask((com.jal.crawler.proto.resolve.ResolveTask) request,
              (io.grpc.stub.StreamObserver<com.jal.crawler.proto.resolve.ResolveTaskResponse>) responseObserver);
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
        METHOD_RESOLVE_TASK);
  }

}
