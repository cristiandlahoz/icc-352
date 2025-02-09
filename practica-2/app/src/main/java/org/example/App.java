package org.example;

import org.example.controllers.*;
import org.example.services.UserService;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.models.User;

public class App {
    public static void main(String[] args) {
        UserService userService = new UserService();

        var app = Javalin.create(config -> {
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/";
                cf.directory = "/public";
            });
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/assets";
                cf.directory = "/assets";
            });
            config.staticFiles.add(cf -> {
                cf.hostedPath = "/fragments";
                cf.directory = "/fragments";

            });

            config.fileRenderer(new JavalinThymeleaf());
        }).start(7777);

        // Controladores
        new ArticleController(app).applyRoutes();
        new AuthenticationController(userService).applyRoutes(app);

        app.before("/", ctx -> {
        });

        app.get("/articles", ArticleController::getAllArticles);
        app.get("/articles/{id}", ArticleController::getArticleById);
        app.post("/articles", ArticleController::createArticle);
        app.post("/articles/{id}", ArticleController::updateArticle);
        app.delete("/articles/{id}", ArticleController::deleteArticle);

        app.get("/users", UserController::getAllUsers);
        app.get("/users/{username}", UserController::getUserByUsername);
        app.post("/users/", UserController::createUser);
        app.put("/users/{username}", UserController::updateUser);
        app.delete("/users/{username}", UserController::deleteUser);

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

            if (!path.equals("/logIn.html") || !path.equals("/signUp.html")) {
                return;
            }

            User user = ctx.sessionAttribute("USUARIO");
            System.out.println("Verificando sesión de usuario en ruta: " + path + " - Usuario en sesión: " + user);

            if (user == null) {
                ctx.redirect("/logIn.html");
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
