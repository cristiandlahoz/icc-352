package org.example.config;

import org.example.models.User;
import org.example.util.Routes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

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
        }).before(ctx -> {
            String path = ctx.path();

            if (!path.equals(Routes.LOGIN.getPath()) && !path.equals(Routes.SIGNUP.getPath())) {
                return;
            }

            User user = ctx.sessionAttribute("USUARIO");
            System.out.println("Verificando sesión de usuario en ruta: " + path + " - Usuario en sesión: " + user);

            if (user == null) {
                ctx.redirect(Routes.LOGIN.getPath());
            }
            System.out.println("Request: " + ctx.method() + " " + ctx.path());
        }).after(ctx -> {
            User user = ctx.sessionAttribute("USUARIO");
            if (user != null) {
                ctx.sessionAttribute("USUARIO", user);
            }
        }).exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.result("Error interno del servidor");
            e.printStackTrace();
        });
    }

}
