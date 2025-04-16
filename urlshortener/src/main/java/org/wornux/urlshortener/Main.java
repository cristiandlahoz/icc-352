package org.wornux.urlshortener;

import io.javalin.Javalin;
import org.wornux.urlshortener.config.AppConfig;
import org.wornux.urlshortener.config.DataSeeder;
import org.wornux.urlshortener.util.EnvReader;

public class Main {
  public static void main(String[] args) {
    // Ensure that the admin user exists in the database.
    DataSeeder.ensureAdmin();

    // Starts a new thread to initialize and run the REST API router.
    new Thread(org.wornux.urlshortener.api.rest.Router::start).start();

    int PORT = EnvReader.getInt("PORT", 7_0_0_0);
    Javalin app = Javalin.create(config -> AppConfig.configureApp(config)).start(PORT);
    AppConfig.ConfigureExceptionHandlers(app);

    // Iniciar gRPC en su propio hilo
    /*
     * new Thread(() -> {
     * try {
     * GrpcServer.start(9090);
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }).start();
     */
  }
}
