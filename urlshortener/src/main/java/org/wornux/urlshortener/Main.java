package org.wornux.urlshortener;

import org.wornux.urlshortener.config.AppConfig;

import io.javalin.Javalin;

public class Main {
  public static void main(String[] args) {
    Javalin app = Javalin.create(
        config -> AppConfig.configureApp(config)).start(7000);

    AppConfig.ConfigureExceptionHandlers(app);

    app.get("/", ctx -> ctx.result("Welcome to URL Shortener"));
  }
}
