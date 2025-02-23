package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.models.User;
import org.example.services.AuthService;
import org.example.util.*;

public class AuthController extends BaseController {

  private final AuthService authService;

  public AuthController(Javalin app, AuthService authService) {
    super(app);
    this.authService = authService;
  }

  public void applyRoutes() {
    app.get(Routes.LOGIN.getPath(), this::renderLoginPage);
    app.get(Routes.SIGNUP.getPath(), this::renderSignupPage);
    app.post(Routes.LOGIN.getPath(), this::login);
    app.post(Routes.LOGOUT.getPath(), this::logout);
    app.post(Routes.SIGNUP.getPath(), this::signup);
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

    authService
        .authenticate(username, password)
        .ifPresentOrElse(
            user -> {
              ctx.sessionAttribute(SessionKeys.USER.getKey(), user);
              ctx.redirect(Routes.HOME.getPath());
            },
            () -> ctx.redirect(Routes.LOGIN.getPath()));
  }

  private void logout(Context ctx) {
    ctx.req().getSession().invalidate();
    ctx.redirect(Routes.HOME.getPath());
  }

  public void signup(Context ctx) {
    String username = ctx.formParam("username");
    String name = ctx.formParam("name");
    String password = ctx.formParam("password");
    boolean isAuthor = ctx.formParam("is_author") != null;
    Role role = isAuthor ? Role.AUTHOR : Role.USER;

    User newUser = new User(username, name, password, role, AccessStatus.AUTHENTICATED);

    try {
      authService.register(newUser);
      ctx.sessionAttribute(SessionKeys.USER.getKey(), newUser);
      ctx.redirect(Routes.HOME.getPath());
    } catch (IllegalArgumentException e) {
      ctx.redirect(Routes.SIGNUP.getPath());
    }
  }
}
