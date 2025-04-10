package edu.pucmm.eict.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.39.0)",
    comments = "Source: asignacion-aula-7.proto")
public final class EstudianteServiceGrpc {

  private EstudianteServiceGrpc() {}

  public static final String SERVICE_NAME = "EstudianteService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteRequest,
      edu.pucmm.eict.grpc.EstudianteReply> getObtenerEstudianteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ObtenerEstudiante",
      requestType = edu.pucmm.eict.grpc.EstudianteRequest.class,
      responseType = edu.pucmm.eict.grpc.EstudianteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteRequest,
      edu.pucmm.eict.grpc.EstudianteReply> getObtenerEstudianteMethod() {
    io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteRequest, edu.pucmm.eict.grpc.EstudianteReply> getObtenerEstudianteMethod;
    if ((getObtenerEstudianteMethod = EstudianteServiceGrpc.getObtenerEstudianteMethod) == null) {
      synchronized (EstudianteServiceGrpc.class) {
        if ((getObtenerEstudianteMethod = EstudianteServiceGrpc.getObtenerEstudianteMethod) == null) {
          EstudianteServiceGrpc.getObtenerEstudianteMethod = getObtenerEstudianteMethod =
              io.grpc.MethodDescriptor.<edu.pucmm.eict.grpc.EstudianteRequest, edu.pucmm.eict.grpc.EstudianteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ObtenerEstudiante"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.EstudianteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.EstudianteReply.getDefaultInstance()))
              .setSchemaDescriptor(new EstudianteServiceMethodDescriptorSupplier("ObtenerEstudiante"))
              .build();
        }
      }
    }
    return getObtenerEstudianteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.Empty,
      edu.pucmm.eict.grpc.EstudianteList> getListarEstudiantesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListarEstudiantes",
      requestType = edu.pucmm.eict.grpc.Empty.class,
      responseType = edu.pucmm.eict.grpc.EstudianteList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.Empty,
      edu.pucmm.eict.grpc.EstudianteList> getListarEstudiantesMethod() {
    io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.Empty, edu.pucmm.eict.grpc.EstudianteList> getListarEstudiantesMethod;
    if ((getListarEstudiantesMethod = EstudianteServiceGrpc.getListarEstudiantesMethod) == null) {
      synchronized (EstudianteServiceGrpc.class) {
        if ((getListarEstudiantesMethod = EstudianteServiceGrpc.getListarEstudiantesMethod) == null) {
          EstudianteServiceGrpc.getListarEstudiantesMethod = getListarEstudiantesMethod =
              io.grpc.MethodDescriptor.<edu.pucmm.eict.grpc.Empty, edu.pucmm.eict.grpc.EstudianteList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListarEstudiantes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.EstudianteList.getDefaultInstance()))
              .setSchemaDescriptor(new EstudianteServiceMethodDescriptorSupplier("ListarEstudiantes"))
              .build();
        }
      }
    }
    return getListarEstudiantesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteReply,
      edu.pucmm.eict.grpc.EstudianteReply> getCrearEstudianteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CrearEstudiante",
      requestType = edu.pucmm.eict.grpc.EstudianteReply.class,
      responseType = edu.pucmm.eict.grpc.EstudianteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteReply,
      edu.pucmm.eict.grpc.EstudianteReply> getCrearEstudianteMethod() {
    io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteReply, edu.pucmm.eict.grpc.EstudianteReply> getCrearEstudianteMethod;
    if ((getCrearEstudianteMethod = EstudianteServiceGrpc.getCrearEstudianteMethod) == null) {
      synchronized (EstudianteServiceGrpc.class) {
        if ((getCrearEstudianteMethod = EstudianteServiceGrpc.getCrearEstudianteMethod) == null) {
          EstudianteServiceGrpc.getCrearEstudianteMethod = getCrearEstudianteMethod =
              io.grpc.MethodDescriptor.<edu.pucmm.eict.grpc.EstudianteReply, edu.pucmm.eict.grpc.EstudianteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CrearEstudiante"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.EstudianteReply.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.EstudianteReply.getDefaultInstance()))
              .setSchemaDescriptor(new EstudianteServiceMethodDescriptorSupplier("CrearEstudiante"))
              .build();
        }
      }
    }
    return getCrearEstudianteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteRequest,
      edu.pucmm.eict.grpc.DeleteResponse> getBorrarEstudianteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BorrarEstudiante",
      requestType = edu.pucmm.eict.grpc.EstudianteRequest.class,
      responseType = edu.pucmm.eict.grpc.DeleteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteRequest,
      edu.pucmm.eict.grpc.DeleteResponse> getBorrarEstudianteMethod() {
    io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.EstudianteRequest, edu.pucmm.eict.grpc.DeleteResponse> getBorrarEstudianteMethod;
    if ((getBorrarEstudianteMethod = EstudianteServiceGrpc.getBorrarEstudianteMethod) == null) {
      synchronized (EstudianteServiceGrpc.class) {
        if ((getBorrarEstudianteMethod = EstudianteServiceGrpc.getBorrarEstudianteMethod) == null) {
          EstudianteServiceGrpc.getBorrarEstudianteMethod = getBorrarEstudianteMethod =
              io.grpc.MethodDescriptor.<edu.pucmm.eict.grpc.EstudianteRequest, edu.pucmm.eict.grpc.DeleteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BorrarEstudiante"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.EstudianteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.DeleteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EstudianteServiceMethodDescriptorSupplier("BorrarEstudiante"))
              .build();
        }
      }
    }
    return getBorrarEstudianteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EstudianteServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EstudianteServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EstudianteServiceStub>() {
        @java.lang.Override
        public EstudianteServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EstudianteServiceStub(channel, callOptions);
        }
      };
    return EstudianteServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EstudianteServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EstudianteServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EstudianteServiceBlockingStub>() {
        @java.lang.Override
        public EstudianteServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EstudianteServiceBlockingStub(channel, callOptions);
        }
      };
    return EstudianteServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EstudianteServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EstudianteServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EstudianteServiceFutureStub>() {
        @java.lang.Override
        public EstudianteServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EstudianteServiceFutureStub(channel, callOptions);
        }
      };
    return EstudianteServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class EstudianteServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void obtenerEstudiante(edu.pucmm.eict.grpc.EstudianteRequest request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getObtenerEstudianteMethod(), responseObserver);
    }

    /**
     */
    public void listarEstudiantes(edu.pucmm.eict.grpc.Empty request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListarEstudiantesMethod(), responseObserver);
    }

    /**
     */
    public void crearEstudiante(edu.pucmm.eict.grpc.EstudianteReply request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCrearEstudianteMethod(), responseObserver);
    }

    /**
     */
    public void borrarEstudiante(edu.pucmm.eict.grpc.EstudianteRequest request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.DeleteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBorrarEstudianteMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getObtenerEstudianteMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.pucmm.eict.grpc.EstudianteRequest,
                edu.pucmm.eict.grpc.EstudianteReply>(
                  this, METHODID_OBTENER_ESTUDIANTE)))
          .addMethod(
            getListarEstudiantesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.pucmm.eict.grpc.Empty,
                edu.pucmm.eict.grpc.EstudianteList>(
                  this, METHODID_LISTAR_ESTUDIANTES)))
          .addMethod(
            getCrearEstudianteMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.pucmm.eict.grpc.EstudianteReply,
                edu.pucmm.eict.grpc.EstudianteReply>(
                  this, METHODID_CREAR_ESTUDIANTE)))
          .addMethod(
            getBorrarEstudianteMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.pucmm.eict.grpc.EstudianteRequest,
                edu.pucmm.eict.grpc.DeleteResponse>(
                  this, METHODID_BORRAR_ESTUDIANTE)))
          .build();
    }
  }

  /**
   */
  public static final class EstudianteServiceStub extends io.grpc.stub.AbstractAsyncStub<EstudianteServiceStub> {
    private EstudianteServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EstudianteServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EstudianteServiceStub(channel, callOptions);
    }

    /**
     */
    public void obtenerEstudiante(edu.pucmm.eict.grpc.EstudianteRequest request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getObtenerEstudianteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listarEstudiantes(edu.pucmm.eict.grpc.Empty request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListarEstudiantesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void crearEstudiante(edu.pucmm.eict.grpc.EstudianteReply request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCrearEstudianteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void borrarEstudiante(edu.pucmm.eict.grpc.EstudianteRequest request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.DeleteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBorrarEstudianteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class EstudianteServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<EstudianteServiceBlockingStub> {
    private EstudianteServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EstudianteServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EstudianteServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.pucmm.eict.grpc.EstudianteReply obtenerEstudiante(edu.pucmm.eict.grpc.EstudianteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getObtenerEstudianteMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.pucmm.eict.grpc.EstudianteList listarEstudiantes(edu.pucmm.eict.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListarEstudiantesMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.pucmm.eict.grpc.EstudianteReply crearEstudiante(edu.pucmm.eict.grpc.EstudianteReply request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCrearEstudianteMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.pucmm.eict.grpc.DeleteResponse borrarEstudiante(edu.pucmm.eict.grpc.EstudianteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBorrarEstudianteMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class EstudianteServiceFutureStub extends io.grpc.stub.AbstractFutureStub<EstudianteServiceFutureStub> {
    private EstudianteServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EstudianteServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EstudianteServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.pucmm.eict.grpc.EstudianteReply> obtenerEstudiante(
        edu.pucmm.eict.grpc.EstudianteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getObtenerEstudianteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.pucmm.eict.grpc.EstudianteList> listarEstudiantes(
        edu.pucmm.eict.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListarEstudiantesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.pucmm.eict.grpc.EstudianteReply> crearEstudiante(
        edu.pucmm.eict.grpc.EstudianteReply request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCrearEstudianteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.pucmm.eict.grpc.DeleteResponse> borrarEstudiante(
        edu.pucmm.eict.grpc.EstudianteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBorrarEstudianteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_OBTENER_ESTUDIANTE = 0;
  private static final int METHODID_LISTAR_ESTUDIANTES = 1;
  private static final int METHODID_CREAR_ESTUDIANTE = 2;
  private static final int METHODID_BORRAR_ESTUDIANTE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EstudianteServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(EstudianteServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_OBTENER_ESTUDIANTE:
          serviceImpl.obtenerEstudiante((edu.pucmm.eict.grpc.EstudianteRequest) request,
              (io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteReply>) responseObserver);
          break;
        case METHODID_LISTAR_ESTUDIANTES:
          serviceImpl.listarEstudiantes((edu.pucmm.eict.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteList>) responseObserver);
          break;
        case METHODID_CREAR_ESTUDIANTE:
          serviceImpl.crearEstudiante((edu.pucmm.eict.grpc.EstudianteReply) request,
              (io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.EstudianteReply>) responseObserver);
          break;
        case METHODID_BORRAR_ESTUDIANTE:
          serviceImpl.borrarEstudiante((edu.pucmm.eict.grpc.EstudianteRequest) request,
              (io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.DeleteResponse>) responseObserver);
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

  private static abstract class EstudianteServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EstudianteServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.pucmm.eict.grpc.EstudianteServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EstudianteService");
    }
  }

  private static final class EstudianteServiceFileDescriptorSupplier
      extends EstudianteServiceBaseDescriptorSupplier {
    EstudianteServiceFileDescriptorSupplier() {}
  }

  private static final class EstudianteServiceMethodDescriptorSupplier
      extends EstudianteServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    EstudianteServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EstudianteServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EstudianteServiceFileDescriptorSupplier())
              .addMethod(getObtenerEstudianteMethod())
              .addMethod(getListarEstudiantesMethod())
              .addMethod(getCrearEstudianteMethod())
              .addMethod(getBorrarEstudianteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
