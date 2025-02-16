package org.example;

import java.util.List;

import org.example.config.AppConfig;
import org.example.config.EnvConfig;
import org.example.controllers.ArticleController;
import org.example.controllers.AuthenticationController;
import org.example.controllers.CommentController;
import org.example.controllers.TagController;
import org.example.controllers.UserController;

import io.javalin.Javalin;

public class App {
    /**
     * @param args
     */
    public static void main(String[] args) {
        int port = EnvConfig.getInt("PORT", 8080);
        Javalin app = AppConfig.createApp().start(port);

        List.of(
                new ArticleController(app),
                new AuthenticationController(app),
                new UserController(app),
                new CommentController(app),
                new TagController(app)).forEach(controller -> controller.applyRoutes());

    }
}
