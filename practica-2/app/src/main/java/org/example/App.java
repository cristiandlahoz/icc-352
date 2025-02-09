package org.example;

import org.example.controllers.ArticleController;
import org.example.controllers.CommentController;
import org.example.controllers.TagController;
import org.example.controllers.UserController;
import org.example.services.UserService;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.models.User;

public class App {
    public static void main(String[] args) {
        UserService userService = new UserService(); // Instancia de UserService

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
        }).start(7777);

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

        app.get("/tags", TagController::getAllTags);
        app.post("/tags", TagController::createTag);

        new ArticleController(app).applyRoutes();

        ///////////////////////////////////////////////

        // Middleware para verificar sesión antes de acceder a ciertas rutas
        app.before(ctx -> {
            String path = ctx.path();

            if (path.equals("/") || path.equals("/login") || path.equals("/logIn.html") ||
                    path.equals("/signUp.html") || path.equals("/signup") || // Agregado `/signup`
                    path.startsWith("/assets") || path.startsWith("/public") || path.startsWith("/articles")) {
                return; // Permitir acceso sin autenticación
            }

            // Verificar si el usuario está autenticado
            User user = ctx.sessionAttribute("USUARIO");
            System.out.println("Verificando sesión de usuario en ruta: " + path + " - Usuario en sesión: " + user);

            if (user == null) {
                ctx.redirect("/logIn.html");
            }
        });

        // Ruta para iniciar sesión con validación de usuario en UserService
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            if (username == null || password == null) {
                ctx.redirect("/logIn.html?error=missing_fields");
                return;
            }

            try {
                User user = userService.getUserByUsername(username);

                // Verificar si la contraseña ingresada coincide
                if (!user.getPassword().equals(password)) {
                    ctx.redirect("/logIn.html?error=invalid_credentials");
                    return;
                }

                // Guardar usuario en la sesión y redirigir a la página principal
                ctx.sessionAttribute("USUARIO", user);
                System.out.println("Usuario autenticado: " + user);
                ctx.redirect("/");

            } catch (IllegalArgumentException e) {
                ctx.redirect("/logIn.html?error=user_not_found");
            }
        });

        // Ruta para cerrar sesión
        app.get("/logout", ctx -> {
            ctx.req().getSession().invalidate();
            ctx.redirect("/logIn.html");
        });

        // Ruta para registrar usuarios (Sign Up)
        app.post("/signup", ctx -> {
            String name = ctx.formParam("name");
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            boolean isAuthor = ctx.formParam("is_author") != null;

            if (name == null || username == null || password == null) {
                ctx.redirect("/signUp.html?error=missing_fields");
                return;
            }

            // Verificar si el usuario ya existe
            try {
                userService.getUserByUsername(username);
                ctx.redirect("/signUp.html?error=user_exists");
                return;
            } catch (IllegalArgumentException e) {
                // Usuario no encontrado, se puede crear
            }

            // Crear nuevo usuario
            User newUser = new User(username, name, password, false, isAuthor);
            userService.createUser(newUser);

            // Verificar si el usuario realmente se guardó en la base de datos antes de redirigir
            User createdUser = userService.getUserByUsername(username);
            if (createdUser != null) {
                ctx.sessionAttribute("USUARIO", createdUser);
                System.out.println("Usuario registrado: " + username);
                System.out.println("Usuario autenticado tras registro: " + ctx.sessionAttribute("USUARIO"));
                ctx.redirect("/"); // Redirigir al blog
            } else {
                ctx.redirect("/signUp.html?error=registration_failed"); // Si falló el registro, regresar al sign-up
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
