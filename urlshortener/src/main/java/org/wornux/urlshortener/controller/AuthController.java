package org.wornux.urlshortener.controller;

import org.wornux.urlshortener.controller.base.BaseController;
import org.wornux.urlshortener.enums.Roles;
import org.wornux.urlshortener.enums.Routes;
import org.wornux.urlshortener.enums.SessionKeys;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.UserService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class AuthController extends BaseController {

  private final UserService userService;

  public AuthController(Javalin app, UserService userService) {
    super(app);
    this.userService = userService;
  }

  @Override
  public void mapEndpoints() {
    app.get(Routes.USER_LOGIN.getRoute(), this::renderLoginPage);
    app.get(Routes.USER_REGISTER.getRoute(), this::renderSignupPage);
    app.post(Routes.USER_LOGIN.getRoute(), this::login);
    app.post(Routes.USER_LOGOUT.getRoute(), this::logout);
    app.post(Routes.USER_REGISTER.getRoute(), this::signup);
  }

  private void renderLoginPage(Context ctx) {
    ctx.render("/auth/login.html");
  }

  private void renderSignupPage(Context ctx) {
    ctx.render("/auth/signup.html");
  }

  private void login(Context ctx) {
    String username = ctx.formParam("username");
    String password = ctx.formParam("password");

    userService
        .authenticate(username, password)
        .ifPresentOrElse(
            user -> {
              System.out.println("✅ Usuario autenticado: " + user.getUsername());
              ctx.sessionAttribute(SessionKeys.USER.getKey(), user);
              ctx.redirect(Routes.HOME.getRoute());
            },
            () -> {
              System.out.println("❌ Error: Usuario no autenticado.");
              ctx.redirect(Routes.USER_LOGIN.getRoute());
            });
  }

  private void logout(Context ctx) {
    ctx.req().getSession().invalidate();
    ctx.redirect(Routes.USER_LOGIN.getRoute());
  }

  public void signup(Context ctx) {
    String username = ctx.formParam("username");
    String email = ctx.formParam("email");
    String password = ctx.formParam("password");

    Roles role = Roles.USER;

    User newUser = new User(username, password, email, role);

    try {
      userService.saveUser(newUser);
      System.out.println("✅ Usuario registrado correctamente: " + username);
      ctx.sessionAttribute(SessionKeys.USER.getKey(), newUser);
      ctx.redirect(Routes.HOME.getRoute());
    } catch (IllegalArgumentException e) {
      System.out.println("❌ Error al registrar el usuario: " + e.getMessage());
      ctx.redirect(Routes.USER_REGISTER.getRoute());
    }
  }
}
