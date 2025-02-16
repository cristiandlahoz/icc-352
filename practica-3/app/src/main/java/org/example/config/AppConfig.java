package org.example.config;

import org.example.models.User;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class AppConfig {
    public static Javalin createApp() {

        return Javalin.create(config -> {
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/";
                cf.directory = "/public";
            });
            config.fileRenderer(new JavalinThymeleaf());
        }).before(ctx -> {
            String path = ctx.path();

            if (!path.equals("/templates/logIn.html") || !path.equals("/templates/signUp.html")) {
                return;
            }

            User user = ctx.sessionAttribute("USUARIO");
            System.out.println("Verificando sesión de usuario en ruta: " + path + " - Usuario en sesión: " + user);

            if (user == null) {
                ctx.redirect("/templates/logIn.html");
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
