package org.wornux.urlshortener.api.rest;

import io.javalin.Javalin;
import org.wornux.urlshortener.config.AppConfig;
import org.wornux.urlshortener.config.DependencyConfig;
import org.wornux.urlshortener.controller.UserController;
import org.wornux.urlshortener.enums.Role;
import org.wornux.urlshortener.api.rest.security.AuthMiddleware;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.util.EnvReader;

import static io.javalin.apibuilder.ApiBuilder.*;
public class Router {

    public static void start() {
        int PORT = EnvReader.getInt("PORT", 7000);
        DependencyConfig.init();
        UserController userController = new UserController(null, DependencyConfig.getUserService());

        Javalin app = Javalin.create(config -> {
            AppConfig.configureApp(config);
            config.router.mount(router -> {
                router.beforeMatched(AuthMiddleware::handleAccess);
            }).apiBuilder(() -> {

                // 🟢 Ruta pública para login (no requiere token)
                path("auth", () -> {
                    post("login", AuthController.loginPost, Role.ANYONE);
                });

                // 🔒 Rutas protegidas
                get("/", ctx -> ctx.redirect("/users"), Role.ANYONE);
                path("users", () -> {
                    get(userController::getAllUsers, Role.ANYONE);
                    post(userController::createUser, Role.USER_WRITE);
                    path("{userId}", () -> {
                        get(userController::getUser, Role.USER_READ);
                        patch(userController::updateUser, Role.USER_WRITE);
                        delete(userController::deleteUser, Role.USER_WRITE);
                    });
                });
            });
        }).start(PORT);

        AppConfig.ConfigureExceptionHandlers(app);
    }
}
