package org.example;

import java.util.List;
import org.example.config.*;
import org.example.controllers.*;
import org.example.util.*;
import org.example.util.Routes;
import io.javalin.Javalin;

public class App {
    /**
     * @param args
     */
    public static void main(String[] args) {
        int port = EnvConfig.getInt("PORT", 8080);
        Javalin app = AppConfig.createApp().start(port);

        DependencyConfig.init();
        List.of(
                new ArticleController(app, DependencyConfig.getArticleService(), DependencyConfig.getTagService(), DependencyConfig.getCommentService()),
                new AuthController(app, DependencyConfig.getAuthService()),
                new UserController(app, DependencyConfig.getUserService()),
                new CommentController(app, DependencyConfig.getCommentService(), DependencyConfig.getArticleService()),
                new TagController(app, DependencyConfig.getTagService())).forEach(BaseController::applyRoutes);

        app.get(Routes.HOME.getPath(), ctx -> {
            ctx.redirect(Routes.ARTICLES.getPath());
        });
    }
}
