package org.example.controller;

import io.javalin.http.Context;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;
import org.example.util.enums.Role;
import org.example.util.enums.Routes;
import org.example.util.enums.SessionKeys;

@Controller(path = "/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Get(path = "/login")
  public void renderLoginPage(Context ctx) {
    ctx.render("/auth/login.html");
  }

  @Get(path = "/signup")
  public void renderSignupPage(Context ctx) {
    ctx.render("/auth/signup.html");
  }

  @Post(path = "/login")
  public void login(Context ctx) {
    String username = ctx.formParam("username");
    String password = ctx.formParam("password");

    System.out.println("🟡 Intentando autenticación para el usuario: " + username);

    authService
        .authenticate(username, password)
        .ifPresentOrElse(
            user -> {
              System.out.println("✅ Usuario autenticado: " + user.getUsername());
              ctx.sessionAttribute(SessionKeys.USER.getKey(), user);
              ctx.redirect(Routes.HOME.getPath());
            },
            () -> {
              System.out.println("❌ Error: Usuario no autenticado.");
              ctx.redirect(Routes.LOGIN.getPath());
            });
  }

  @Get(path = "/logout")
  public void logout(Context ctx) {
    ctx.req().getSession().invalidate();
    ctx.redirect(Routes.LOGIN.getPath());
  }

  @Post(path = "/signup")
  public void signup(Context ctx) {
    String username = ctx.formParam("username");
    String name = ctx.formParam("name");
    String password = ctx.formParam("password");

    Role role = Role.USER;

    User newUser = new User(username, password, name, role);

    try {
      authService.register(newUser);
      System.out.println("✅ Usuario registrado correctamente: " + username);
      ctx.sessionAttribute(SessionKeys.USER.getKey(), newUser);
      ctx.redirect(Routes.HOME.getPath());
    } catch (IllegalArgumentException e) {
      System.out.println("❌ Error al registrar el usuario: " + e.getMessage());
      ctx.redirect(Routes.SIGNUP.getPath());
    }
  }
}
