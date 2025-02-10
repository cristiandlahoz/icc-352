package org.example;

import org.example.controllers.*;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.models.User;

public class App {
    /**
     * @param args
     */
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/";
                cf.directory = "/public";
            });
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/assets";
                cf.directory = "/assets";
            });

            config.fileRenderer(new JavalinThymeleaf());
        }).start(7789);

        // Controladores
        new ArticleController(app).applyRoutes();
        new AuthenticationController(app).applyRoutes();
        new UserController(app).applyRoutes();

        app.get("/comments", CommentController::getAllComments);
        app.get("/comments/{id}", CommentController::getCommentById);
        app.post("/comments", CommentController::createComment);
        app.put("/comments/{id}", CommentController::updateComment);
        app.delete("/comments/{id}", CommentController::deleteComment);
        app.get("/articles/{articleId}/comments/{commentId}", CommentController::getCommentByArticleAndCommentId);

        app.post("/tags", TagController::createTag);

        // Middleware para verificar sesión antes de acceder a ciertas rutas
        app.before(ctx -> {
            String path = ctx.path();

            if (!path.equals("/templates/logIn.html") || !path.equals("/templates/signUp.html")) {
                return;
            }

            User user = ctx.sessionAttribute("USUARIO");
            System.out.println("Verificando sesión de usuario en ruta: " + path + " - Usuario en sesión: " + user);

            if (user == null) {
                ctx.redirect("/templates/logIn.html");
            }
        });

        // Middleware para persistir la sesión después de cada petición
        app.after(ctx -> {
            User user = ctx.sessionAttribute("USUARIO");
            if (user != null) {
                ctx.sessionAttribute("USUARIO", user);
            }
        });

    }
}
