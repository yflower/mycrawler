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
public class RpcResolveConfigShowGrpc {

  private RpcResolveConfigShowGrpc() {}

  public static final String SERVICE_NAME = "grpc.RpcResolveConfigShow";

  // Static method descriptors that strictly reflect the proto.

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RpcResolveConfigShowStub newStub(io.grpc.Channel channel) {
    return new RpcResolveConfigShowStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RpcResolveConfigShowBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RpcResolveConfigShowBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RpcResolveConfigShowFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RpcResolveConfigShowFutureStub(channel);
  }

  /**
   */
  public static abstract class RpcResolveConfigShowImplBase implements io.grpc.BindableService {

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .build();
    }
  }

  /**
   */
  public static final class RpcResolveConfigShowStub extends io.grpc.stub.AbstractStub<RpcResolveConfigShowStub> {
    private RpcResolveConfigShowStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveConfigShowStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveConfigShowStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveConfigShowStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class RpcResolveConfigShowBlockingStub extends io.grpc.stub.AbstractStub<RpcResolveConfigShowBlockingStub> {
    private RpcResolveConfigShowBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveConfigShowBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveConfigShowBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveConfigShowBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class RpcResolveConfigShowFutureStub extends io.grpc.stub.AbstractStub<RpcResolveConfigShowFutureStub> {
    private RpcResolveConfigShowFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RpcResolveConfigShowFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RpcResolveConfigShowFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RpcResolveConfigShowFutureStub(channel, callOptions);
    }
  }


  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RpcResolveConfigShowImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RpcResolveConfigShowImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
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
    return new io.grpc.ServiceDescriptor(SERVICE_NAME);
  }

}
