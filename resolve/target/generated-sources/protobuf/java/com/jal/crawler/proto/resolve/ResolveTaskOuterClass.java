// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: resolveTask.proto

package com.jal.crawler.proto.resolve;

public final class ResolveTaskOuterClass {
  private ResolveTaskOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_ResolveTask_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_ResolveTask_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_ResolveTask_Var_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_ResolveTask_Var_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_ResolveTask_Item_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_ResolveTask_Item_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_rpc_ResolveTaskResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_rpc_ResolveTaskResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021resolveTask.proto\022\003rpc\032\ntask.proto\"\220\002\n" +
      "\013ResolveTask\022 \n\ttask_type\030\001 \001(\0162\r.rpc.Ta" +
      "skType\022\017\n\007taskTag\030\002 \001(\t\022!\n\003var\030\003 \003(\0132\024.r" +
      "pc.ResolveTask.Var\022#\n\004item\030\004 \003(\0132\025.rpc.R" +
      "esolveTask.Item\032H\n\003Var\022\014\n\004name\030\001 \001(\t\022\r\n\005" +
      "query\030\002 \001(\t\022\016\n\006option\030\003 \001(\t\022\024\n\014option_va" +
      "lue\030\004 \001(\t\032<\n\004Item\022\021\n\titem_name\030\001 \001(\t\022!\n\003" +
      "var\030\002 \003(\0132\024.rpc.ResolveTask.Var\"H\n\023Resol" +
      "veTaskResponse\022 \n\top_status\030\001 \001(\0162\r.rpc." +
      "OPStatus\022\017\n\007taskTag\030\002 \001(\t2K\n\016RpcResolveT",
      "ask\0229\n\013resolveTask\022\020.rpc.ResolveTask\032\030.r" +
      "pc.ResolveTaskResponseB!\n\035com.jal.crawle" +
      "r.proto.resolveP\001b\006proto3"
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
          com.jal.crawler.proto.task.Task.getDescriptor(),
        }, assigner);
    internal_static_rpc_ResolveTask_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_rpc_ResolveTask_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_ResolveTask_descriptor,
        new java.lang.String[] { "TaskType", "TaskTag", "Var", "Item", });
    internal_static_rpc_ResolveTask_Var_descriptor =
      internal_static_rpc_ResolveTask_descriptor.getNestedTypes().get(0);
    internal_static_rpc_ResolveTask_Var_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_ResolveTask_Var_descriptor,
        new java.lang.String[] { "Name", "Query", "Option", "OptionValue", });
    internal_static_rpc_ResolveTask_Item_descriptor =
      internal_static_rpc_ResolveTask_descriptor.getNestedTypes().get(1);
    internal_static_rpc_ResolveTask_Item_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_ResolveTask_Item_descriptor,
        new java.lang.String[] { "ItemName", "Var", });
    internal_static_rpc_ResolveTaskResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_rpc_ResolveTaskResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_rpc_ResolveTaskResponse_descriptor,
        new java.lang.String[] { "OpStatus", "TaskTag", });
    com.jal.crawler.proto.task.Task.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
