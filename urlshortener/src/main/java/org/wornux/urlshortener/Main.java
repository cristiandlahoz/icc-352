package org.wornux.urlshortener;

import io.javalin.Javalin;
import org.wornux.urlshortener.config.AppConfig;
import org.wornux.urlshortener.util.EnvReader;

public class Main {
  public static void main(String[] args) {
    int PORT = EnvReader.getInt("PORT", 7_0_0_0);
    Javalin app = Javalin.create(config -> AppConfig.configureApp(config)).start(PORT);

    AppConfig.ConfigureExceptionHandlers(app);
  }
}
