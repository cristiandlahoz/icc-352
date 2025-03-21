package org.example;

import java.util.List;

import org.example.config.AppConfig;
import org.example.config.DependencyConfig;
import org.example.config.EnvConfig;
import org.example.controllers.ArticleController;
import org.example.controllers.AuthController;
import org.example.controllers.ChatController;
import org.example.controllers.CommentController;
import org.example.controllers.PhotoController;
import org.example.controllers.TagController;
import org.example.controllers.UserController;
import org.example.controllers.api.ArticleApiController;
import org.example.util.AccessStatus;
import org.example.util.BaseController;
import org.example.util.Role;
import org.example.util.Routes;
import org.example.util.StartDatabase;

import io.javalin.Javalin;

public class App {
  /**
   * @param args
   */
  public static void main(final String[] args) {
    final int port = EnvConfig.getInt("PORT", 7_000);
    final Javalin app = AppConfig.createApp().start(port);

    StartDatabase.getInstance().initDatabase();
    DependencyConfig.init();
    List.of(
        new ArticleController(
            app,
            DependencyConfig.getArticleService(),
            DependencyConfig.getTagService(),
            DependencyConfig.getCommentService()),
        new ArticleApiController(
            app,
            DependencyConfig.getArticleService(),
            DependencyConfig.getCommentService()),
        new AuthController(app, DependencyConfig.getAuthService()),
        new UserController(app, DependencyConfig.getUserService()),
        new CommentController(
            app, DependencyConfig.getCommentService(), DependencyConfig.getArticleService()),
        new TagController(app, DependencyConfig.getTagService()),
        new PhotoController(app, DependencyConfig.getPhotoService()),
        new ChatController(app))
        .forEach(BaseController::applyRoutes);
    try {
      DependencyConfig.getUserService()
          .createUser("admin", "Grupo 3", "admin", Role.ADMIN, AccessStatus.AUTHENTICATED, null);
    } catch (final Exception e) {
      System.out.println("Admin user already registered: " + e.getMessage());
    }
    app.get(
        Routes.HOME.getPath(),
        ctx -> {
          ctx.redirect(Routes.ARTICLES.getPath());
        });
  }
}
