package org.wornux.urlshortener;

import io.javalin.Javalin;
import org.wornux.urlshortener.controller.UserController;
import org.wornux.urlshortener.controller.ShortUrlController;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Register routes
        app.get("/", ctx -> ctx.result("Welcome to URL Shortener"));
        UserController.registerRoutes(app);
        ShortUrlController.registerRoutes(app);
    }
}
