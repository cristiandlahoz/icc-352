package org.example;

import java.util.List;

import org.example.config.EnvConfig;
import org.example.controllers.ArticleController;
import org.example.controllers.AuthenticationController;
import org.example.controllers.CommentController;
import org.example.controllers.TagController;
import org.example.controllers.UserController;
import org.example.models.User;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class App {
    /**
     * @param args
     */
    public static void main(String[] args) {
        int port = EnvConfig.getInt("PORT", 8080);
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/";
                cf.directory = "/public";
            });
            config.fileRenderer(new JavalinThymeleaf());
        }).start(port);

        List.of(
                new ArticleController(app),
                new AuthenticationController(app),
                new UserController(app),
                new CommentController(app),
                new TagController(app)).forEach(controller -> controller.applyRoutes());

        app.before(ctx -> {
            String path = ctx.path();

            if (!path.equals("/templates/auth/logIn.html") || !path.equals("/templates/auth/signUp.html")) {
                return;
            }

            User user = ctx.sessionAttribute("USUARIO");
            System.out.println("Verificando sesión de usuario en ruta: " + path + " - Usuario en sesión: " + user);

            if (user == null) {
                ctx.redirect("/templates/auth/logIn.html");
            }
        });

        app.after(ctx -> {
            User user = ctx.sessionAttribute("USUARIO");
            if (user != null) {
                ctx.sessionAttribute("USUARIO", user);
            }
        });

    }
}
