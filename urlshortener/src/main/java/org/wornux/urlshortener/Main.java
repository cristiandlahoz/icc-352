package org.wornux.urlshortener;

import io.javalin.Javalin;
import org.wornux.urlshortener.api.grpc.v1.GrpcServer;
import org.wornux.urlshortener.config.AppConfig;
import org.wornux.urlshortener.util.EnvReader;

public class Main {
  public static void main(String[] args) {
    // Starts a new thread to initialize and run the REST API router.
    new Thread(org.wornux.urlshortener.api.rest.Router::start).start();

    int PORT = EnvReader.getInt("PORT", 7_0_0_0);
    Javalin app = Javalin.create(config -> AppConfig.configureApp(config)).start(PORT);
    AppConfig.ConfigureExceptionHandlers(app);

    new Thread(
            () -> {
              try {
                int GRPC_PORT = EnvReader.getInt("PORT_GRPC", 9090);
                System.out.println("🚀 Servidor gRPC corriendo en el puerto " + GRPC_PORT);
                GrpcServer.start(GRPC_PORT);
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
