package org.example;

import io.javalin.Javalin;

import org.example.config.AppConfig;
import org.example.config.EnvConfig;
import org.example.util.Router;

public class App {
  public static void main(String[] args) {
    int PORT = EnvConfig.getInt("PORT", 7000);
    Javalin app = AppConfig.createApp().start(PORT);
    Router.registerRoutes(app);
  }
}
