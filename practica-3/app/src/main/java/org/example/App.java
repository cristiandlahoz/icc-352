package org.example;

import io.javalin.Javalin;
import java.util.List;
import org.example.config.*;
import org.example.controllers.*;
import org.example.util.*;
import org.example.util.Routes;

public class App {
  /**
   * @param args
   */
  public static void main(String[] args) {
    int port = EnvConfig.getInt("PORT", 8080);
    Javalin app = AppConfig.createApp().start(port);

    StartDatabase.getInstance().initDatabase();
    DependencyConfig.init();
    List.of(
            new ArticleController(
                app,
                DependencyConfig.getArticleService(),
                DependencyConfig.getTagService(),
                DependencyConfig.getCommentService()),
            new AuthController(app, DependencyConfig.getAuthService()),
            new UserController(app, DependencyConfig.getUserService()),
            new CommentController(
                app, DependencyConfig.getCommentService(), DependencyConfig.getArticleService()),
            new TagController(app, DependencyConfig.getTagService()),
            new PhotoController(app, DependencyConfig.getPhotoService()))
        .forEach(BaseController::applyRoutes);
    try {
      DependencyConfig.getUserService()
          .createUser("admin", "cristian", "admin", Role.ADMIN, AccessStatus.AUTHENTICATED, null);
    } catch (Exception e) {
      System.out.println("Admin user already registered: " + e.getMessage());
    }
    app.get(
        Routes.HOME.getPath(),
        ctx -> {
          ctx.redirect(Routes.ARTICLES.getPath());
        });
  }
}
