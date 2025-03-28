package org.wornux.urlshortener.config;

import io.javalin.config.Config;

public class AppConfig {
  public static void configureApp(Config config) {
    // Example: Enable CORS, set default error handling, etc.
    config.enableCorsForAllOrigins();
  }
}
