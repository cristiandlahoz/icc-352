package org.example;

import io.javalin.Javalin;
import org.example.config.AppConfig;
import org.example.config.EnvConfig;
import org.example.model.User;
import org.example.util.Router;
import org.example.util.StartDatabase;
import org.example.util.enums.SessionKeys;

public class App {
  public static void main(String[] args) {
    int PORT = EnvConfig.getInt("PORT", 7000);
    Javalin app = AppConfig.createApp().start(PORT);
    StartDatabase.getInstance().initDatabase();

    //
    app.before(
        ctx -> {
          String path = ctx.path();

          // Permitir el acceso libre a estas rutas
          if (path.equals("/auth/login") || path.equals("/auth/signup") || path.equals("/logout")) {
            return; // Permite el paso sin redirección
          }

          // Verifica si el usuario está autenticado
          User user = ctx.sessionAttribute(SessionKeys.USER.getKey());

          if (user == null) {
            ctx.redirect("/auth/login"); // Redirige a la página de Login si no hay sesión activa
          }
        });

    // Rutas

    Router.registerRoutes(app);

    app.get("/", ctx -> ctx.render("/home.html"));
    app.get("/auth/login", ctx -> ctx.render("/auth/login.html"));
    app.get("/auth/signup", ctx -> ctx.render("/auth/signup.html"));
  }
}
