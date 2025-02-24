package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.servlet.http.Cookie;
import java.util.Base64;
import org.example.models.Photo;
import org.example.models.User;
import org.example.services.AuthService;
import org.example.util.*;
import org.jasypt.util.text.BasicTextEncryptor;

public class AuthController extends BaseController {

  private final AuthService authService;
  private final BasicTextEncryptor textEncryptor;

  public AuthController(Javalin app, AuthService authService) {
    super(app);
    this.authService = authService;
    this.textEncryptor = new BasicTextEncryptor();
    this.textEncryptor.setPassword("superSecretKey");
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
    boolean rememberMe = ctx.formParam("remember_me") != null;

    authService
        .authenticate(username, password)
        .ifPresentOrElse(
            user -> {
              ctx.sessionAttribute(SessionKeys.USER.getKey(), user);

              if (rememberMe && username != null && !username.isEmpty()) {
                // Encripta el username antes de almacenarlo en la cookie
                String encryptedUsername = textEncryptor.encrypt(username);

                Cookie cookie = new Cookie("remember_me", encryptedUsername);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");

                ctx.res().addCookie(cookie);
              }

              ctx.redirect(Routes.HOME.getPath());
            },
            () -> ctx.redirect(Routes.LOGIN.getPath()));
  }

  private void logout(Context ctx) {
    ctx.req().getSession().invalidate();

    Cookie cookie = new Cookie("remember_me", "");
    cookie.setMaxAge(0);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    ctx.res().addCookie(cookie);

    ctx.redirect(Routes.HOME.getPath());
  }

  public void signup(Context ctx) {
    String username = ctx.formParam("username");
    String name = ctx.formParam("name");
    String password = ctx.formParam("password");
    boolean isAuthor = ctx.formParam("is_author") != null;
    Role role = isAuthor ? Role.AUTHOR : Role.USER;

    // Procesar la foto de perfil si se envió
    Photo profilePhoto = null;
    if (ctx.uploadedFile("profilePhoto") != null) {
      try {
        var uploadedFile = ctx.uploadedFile("profilePhoto");
        byte[] bytes = uploadedFile.content().readAllBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        profilePhoto =
            new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
      } catch (Exception e) {
        e.printStackTrace();
        ctx.status(500).result("Error processing profile photo.");
        return;
      }
    }
    if (profilePhoto.getFotoBase64().isEmpty()) {
      profilePhoto = null;
    }
    // Crear el nuevo usuario con la foto de perfil
    User newUser =
        new User(username, name, password, role, AccessStatus.AUTHENTICATED, profilePhoto);

    try {
      authService.register(newUser);
      ctx.sessionAttribute(SessionKeys.USER.getKey(), newUser);
      ctx.redirect(Routes.HOME.getPath());
    } catch (IllegalArgumentException e) {
      ctx.redirect(Routes.SIGNUP.getPath());
    }
  }
}
