package org.wornux.urlshortener.controller;

import org.wornux.urlshortener.controller.base.BaseController;
import org.wornux.urlshortener.core.routing.annotations.*;
import org.wornux.urlshortener.enums.Role;
import org.wornux.urlshortener.enums.Routes;
import org.wornux.urlshortener.enums.SessionKeys;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.UserService;

import io.javalin.http.Context;

/**
 * AuthController is a controller class that handles HTTP requests related to
 * authentication and authorization.
 */
@CONTROLLER(path = "/auth")
public class AuthController extends BaseController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Handles the GET request to render the login page.
   * 
   * @param ctx The context of the HTTP request.
   */
  @GET(path = "/login")
  public void renderLoginPage(Context ctx) {
    ctx.render("/auth/login.html");
  }

  /**
   * Handles the POST request to authenticate a user.
   * 
   * @param ctx The context of the HTTP request.
   */
  @GET(path = "/signup")
  public void renderSignupPage(Context ctx) {
    ctx.render("/auth/signup.html");
  }

  /**
   * Handles the POST request to authenticate a user.
   * 
   * @param ctx The context of the HTTP request.
   */
  @POST(path = "/login")
  public void login(Context ctx) {
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

  /**
   * Handles the GET request to logout a user.
   * 
   * @param ctx The context of the HTTP request.
   */
  @GET(path = "/logout")
  public void logout(Context ctx) {
    ctx.req().getSession().invalidate();
    ctx.redirect(Routes.USER_LOGIN.getRoute());
  }

  /**
   * Handles the POST request to register a new user.
   * 
   * @param ctx The context of the HTTP request.
   */
  @POST(path = "/signup")
  public void signup(Context ctx) {
    String username = ctx.formParam("username");
    String email = ctx.formParam("email");
    String password = ctx.formParam("password");

    Role role = Role.USER;

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
