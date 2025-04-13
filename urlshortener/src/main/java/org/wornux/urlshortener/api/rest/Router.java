package org.wornux.urlshortener.api.rest;

import org.wornux.urlshortener.api.rest.security.AuthMiddleware;
import org.wornux.urlshortener.core.routing.DIContainer;
import org.wornux.urlshortener.util.EnvReader;

import io.javalin.Javalin;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

public class Router {

  public static void start() {
    int PORT = EnvReader.getInt("PORT-REST", 7_0_0_0);

    Javalin app = Javalin.create(config -> {
      config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
        pluginConfig.withDefinitionConfiguration((version, definition) -> {
          definition.withOpenApiInfo(info -> info.setTitle("Javalin OpenAPI example"));
        });
      }));
      config.registerPlugin(new SwaggerPlugin());
      config.registerPlugin(new ReDocPlugin());
      //

      config.router.mount(router -> {
        router.beforeMatched(new AuthMiddleware());
        AuthController authController = DIContainer.get(AuthController.class);
        UserController userController = DIContainer.get(UserController.class);
        UrlController urlController = DIContainer.get(UrlController.class);
        authController.applyRoutes(router);
        userController.applyRoutes(router);
        urlController.applyRoutes(router);
      }).apiBuilder(() -> {
      });

    }).start(PORT);

    var userService = DIContainer.get(org.wornux.urlshortener.service.UserService.class);
    var urlService = DIContainer.get(org.wornux.urlshortener.service.UrlService.class);

    userService.getUsers().stream()
            .filter(user -> "eliana".equalsIgnoreCase(user.getUsername()))
            .findFirst()
            .ifPresent(user -> {
              System.out.println("🆔 El ID de Eliana es: " + user.getId());
            });

    System.out.println("📦 URLs ya existentes en la base de datos:");
    urlService.getAllShortenedUrls().forEach(url -> {
      String creator = (url.createdBy() != null && url.createdBy().getUsername() != null)
              ? url.createdBy().getUsername()
              : "🕵️ Desconocido";
      System.out.println("─────────────────────────────");
      System.out.println("ID:           " + url.id());
      System.out.println("Original:     " + url.originalUrl());
      System.out.println("Acortada:     " + url.shortenedUrl());
      System.out.println("Creada:       " + url.createdAt());
      System.out.println("Clicks:       " + url.clickCount());
      System.out.println("Creador:     " + creator + url.createdBy().getId());
    });


  }
}
