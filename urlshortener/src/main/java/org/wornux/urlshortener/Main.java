package org.wornux.urlshortener;

import org.wornux.urlshortener.api.rest.Router;


public class Main {
  public static void main(String[] args) {
    // Iniciar REST en su propio hilo
    new Thread(Router::start).start();

    // Iniciar gRPC en su propio hilo
    /*new Thread(() -> {
      try {
        GrpcServer.start(9090);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();*/
  }
}
