package org.example;

import io.javalin.Javalin;

public class App {
  public static void main(String[] args) {
    int PORT = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7000;
    Javalin app = Javalin.create(config -> {
      config.staticFiles.add(cf -> {
        cf.hostedPath = "/";
        cf.directory = "/public";
      });
    }).start(PORT);

    app.get("/", ctx -> ctx.result("Hello World"));
  }
}
