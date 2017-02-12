// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: resolveConfig.proto

package com.jal.crawler.proto.resolve;

public final class ResolveConfigOuterClass {
  private ResolveConfigOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpc_ResolveConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpc_ResolveConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpc_ResolveConfig_MongoConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpc_ResolveConfig_MongoConfig_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023resolveConfig.proto\022\004grpc\032\022configCommo" +
      "n.proto\032\025configComponent.proto\"\250\002\n\rResol" +
      "veConfig\022\016\n\006thread\030\001 \001(\005\022,\n\007persist\030\002 \001(" +
      "\0162\033.grpc.ResolveConfig.Persist\0225\n\014mongo_" +
      "config\030\003 \001(\0132\037.grpc.ResolveConfig.MongoC" +
      "onfig\022\"\n\014redis_config\030\004 \001(\0132\014.RedisConfi" +
      "g\032[\n\013MongoConfig\022\014\n\004host\030\001 \001(\t\022\014\n\004port\030\002" +
      " \001(\005\022\014\n\004user\030\003 \001(\t\022\020\n\010password\030\004 \001(\t\022\020\n\010" +
      "database\030\005 \001(\t\"!\n\007Persist\022\t\n\005MONGO\020\000\022\013\n\007" +
      "CONSOLE\020\0012\211\001\n\020RpcResolveConfig\0223\n\rresolv",
      "eConfig\022\023.grpc.ResolveConfig\032\r.ConfigSta" +
      "tus\022@\n\021resolveConfigShow\022\026.ConfigCompone" +
      "ntStatus\032\023.grpc.ResolveConfig2\026\n\024RpcReso" +
      "lveConfigShowB!\n\035com.jal.crawler.proto.r" +
      "esolveP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.jal.crawler.proto.config.ConfigCommon.getDescriptor(),
          com.jal.crawler.proto.configComponnet.ConfigComponent.getDescriptor(),
        }, assigner);
    internal_static_grpc_ResolveConfig_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_grpc_ResolveConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpc_ResolveConfig_descriptor,
        new java.lang.String[] { "Thread", "Persist", "MongoConfig", "RedisConfig", });
    internal_static_grpc_ResolveConfig_MongoConfig_descriptor =
      internal_static_grpc_ResolveConfig_descriptor.getNestedTypes().get(0);
    internal_static_grpc_ResolveConfig_MongoConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpc_ResolveConfig_MongoConfig_descriptor,
        new java.lang.String[] { "Host", "Port", "User", "Password", "Database", });
    com.jal.crawler.proto.config.ConfigCommon.getDescriptor();
    com.jal.crawler.proto.configComponnet.ConfigComponent.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
