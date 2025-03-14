package org.example;

import io.javalin.Javalin;
import org.example.config.AppConfig;
import org.example.config.EnvConfig;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.*;
import org.example.util.enums.*;

public class App {
  public static void main(String[] args) {
    int PORT = EnvConfig.getInt("PORT", 7000);
    Javalin app = AppConfig.createApp().start(PORT);
    StartDatabase.getInstance().initDatabase();

    /**
     * @param app
     * @see Router#registerRoutes(Javalin)
     * @see DIContainer#get(Class)
     * @see UserService#createUser(String, String, String, Role)
     * @see Role#ADMIN
     */
    Router.registerRoutes(app);
    try {
      DIContainer.get(UserService.class).createUser("admin", "admin", "admin", Role.ADMIN);
    } catch (Exception e) {
      System.out.println("Error creating admin user");
    }

    app.get("/", ctx -> ctx.render("/home.html"));
  }
}
