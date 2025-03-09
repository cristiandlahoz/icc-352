package org.example;

import org.example.util.Router;

import io.javalin.Javalin;

public class App {
  public static void main(String[] args) {
    int PORT = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7000;
    Javalin app = Javalin.create().start(PORT);
    Router.registerRoutes(app);

  }
}
