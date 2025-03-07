package org.example;

import io.javalin.Javalin;

public class App {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.staticFiles.add(cf -> {
        cf.hostedPath = "/";
        cf.directory = "/public";
      });
    }).start(8088);

    app.get("/", ctx -> ctx.result("Hello World"));
  }
}
