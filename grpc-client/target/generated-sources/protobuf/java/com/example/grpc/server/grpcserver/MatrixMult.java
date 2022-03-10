// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: matrix.proto

package com.example.grpc.server.grpcserver;

public final class MatrixMult {
  private MatrixMult() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_matrixmult_Request_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_matrixmult_Request_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_matrixmult_MatrixRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_matrixmult_MatrixRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_matrixmult_MatrixReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_matrixmult_MatrixReply_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_matrixmult_Element_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_matrixmult_Element_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_matrixmult_Row_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_matrixmult_Row_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014matrix.proto\022\nmatrixmult\"c\n\007Request\022+\n" +
      "\010request1\030\001 \001(\0132\031.matrixmult.MatrixReque" +
      "st\022+\n\010request2\030\002 \001(\0132\031.matrixmult.Matrix" +
      "Request\"R\n\rMatrixRequest\022\035\n\004rows\030\001 \003(\0132\017" +
      ".matrixmult.Row\022\020\n\010rowCount\030\002 \001(\005\022\020\n\010col" +
      "Count\030\003 \001(\005\",\n\013MatrixReply\022\035\n\004rows\030\001 \003(\013" +
      "2\017.matrixmult.Row\"\024\n\007Element\022\t\n\001x\030\001 \001(\005\"" +
      "\027\n\003Row\022\020\n\010elements\030\n \003(\0052\214\001\n\rMatrixServi" +
      "ce\022?\n\rMultiplyBlock\022\023.matrixmult.Request" +
      "\032\027.matrixmult.MatrixReply\"\000\022:\n\010AddBlock\022",
      "\023.matrixmult.Request\032\027.matrixmult.Matrix" +
      "Reply\"\000B7\n\"com.example.grpc.server.grpcs" +
      "erverB\nMatrixMultP\001\242\002\002MMb\006proto3"
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
        }, assigner);
    internal_static_matrixmult_Request_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_matrixmult_Request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_matrixmult_Request_descriptor,
        new java.lang.String[] { "Request1", "Request2", });
    internal_static_matrixmult_MatrixRequest_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_matrixmult_MatrixRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_matrixmult_MatrixRequest_descriptor,
        new java.lang.String[] { "Rows", "RowCount", "ColCount", });
    internal_static_matrixmult_MatrixReply_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_matrixmult_MatrixReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_matrixmult_MatrixReply_descriptor,
        new java.lang.String[] { "Rows", });
    internal_static_matrixmult_Element_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_matrixmult_Element_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_matrixmult_Element_descriptor,
        new java.lang.String[] { "X", });
    internal_static_matrixmult_Row_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_matrixmult_Row_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_matrixmult_Row_descriptor,
        new java.lang.String[] { "Elements", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
