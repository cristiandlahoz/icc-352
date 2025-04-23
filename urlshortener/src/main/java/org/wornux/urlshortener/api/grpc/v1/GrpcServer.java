package org.wornux.urlshortener.api.grpc.v1;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.wornux.urlshortener.core.routing.DIContainer;

public class GrpcServer {

  public static void start(int port) throws Exception {
    var userService = DIContainer.get(org.wornux.urlshortener.service.UserService.class);
    var urlService = DIContainer.get(org.wornux.urlshortener.service.UrlService.class);

    Server server =
        ServerBuilder.forPort(port)
            .addService(new UrlShortenerServiceImpl(urlService, userService))
            .addService(ProtoReflectionService.newInstance())
            .build()
            .start();

    System.out.println("✅ gRPC Server iniciado en el puerto " + port);

    // Mantener el servidor activo
    server.awaitTermination();
  }
}
