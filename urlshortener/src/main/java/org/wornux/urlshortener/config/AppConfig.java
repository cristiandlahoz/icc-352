package org.wornux.urlshortener.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinThymeleaf;

public class AppConfig {
  public static void configureApp(JavalinConfig config) {
    config.staticFiles.add(stc -> {
      stc.hostedPath = "/";
      stc.directory = "/public";
    });
    config.fileRenderer(templateEngineConfig());
  }

  public static JavalinThymeleaf templateEngineConfig() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("/templates/");

    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);

    JavalinThymeleaf thymeleafRenderer = new JavalinThymeleaf(templateEngine);
    return thymeleafRenderer;
  }

  public static void ConfigureExceptionHandlers(Javalin app) {
    app.error(404, ctx -> {
      ctx.render("pages/404.html");
    });
  }
}
