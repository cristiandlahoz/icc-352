package org.example.config;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class AppConfig {
    public static Javalin createApp() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        JavalinThymeleaf thymeleafRenderer = new JavalinThymeleaf(templateEngine);

        return Javalin.create(config -> {
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/";
                cf.directory = "/public";
            });
            config.fileRenderer(thymeleafRenderer);
        });
    }
}
