package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.IOException;
import java.util.*;
import org.example.models.Photo;
import org.example.models.User;
import org.example.services.UserService;
import org.example.util.*;
import org.hibernate.Hibernate;

public class UserController extends BaseController {
  private final UserService userService;

  public UserController(Javalin app, UserService userService) {
    super(app);
    this.userService = userService;
  }

  public void applyRoutes() {
    app.get(Routes.CREATEUSER.getPath(), this::renderCreateUserPage);
    app.get(Routes.MANAGEUSERS.getPath(), this::renderManageUsers);
    app.get(Routes.USERS.getPath(), this::getAllUsers);
    app.get(Routes.USER.getPath(), this::getUserByUsername);
    app.post(Routes.USERS.getPath(), this::createUser);
    app.post(Routes.USER.getPath(), this::updateUser);
    app.delete(Routes.USER.getPath(), this::deleteUser);
    app.post("/users/form/{id}", this::formHandler);
  }

  private void renderCreateUserPage(Context ctx) {
    ctx.render("/pages/create_user.html");
  }

  private void renderManageUsers(Context ctx) {
    ctx.render("/pages/manage_users.html");
  }

  public void getAllUsers(Context ctx) {
    Collection<User> myUsers = userService.getAllUsers();
    myUsers.forEach(
        user -> {
          if (user.getProfilePhoto() != null) {
            Hibernate.initialize(user.getProfilePhoto()); // Fuerza la carga de profilePhoto
          }
        });
    ctx.json(myUsers);
  }

  public void getUserByUsername(Context ctx) {
    String username = ctx.pathParam("username");
    User myUser = userService.getUserByUsername(username).orElse(null);
    ctx.json(myUser);
  }

  public void createUser(Context ctx) {
    ctx.attribute(
        "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));
    User newUser = processUserForm(ctx, Routes.CREATEUSER.getPath());
    if (newUser != null) {
      ctx.redirect("/");
    } else {
      ctx.status(400).result("Error Creating User");
    }
  }

  public void updateUser(Context ctx) {
    ctx.attribute(
        "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));
    try {
      String username = ctx.pathParam("username");
      userService
          .getUserByUsername(username)
          .ifPresentOrElse(
              existingUser -> {
                String role = existingUser.getRole().toString();
                Map<String, Object> model = setModel("user", existingUser, "role", role);
                ctx.render("/pages/update_user.html", model);
              },
              () -> ctx.status(400).result("Error Updating User"));
    } catch (Exception e) {
      ctx.status(500).result("Error Updating User" + e.getMessage());
    }
  }

  public void formHandler(Context ctx) {
    Long userId = Long.parseLong(ctx.pathParam("id"));
    String name = ctx.formParam("name");
    String username = ctx.formParam("username");
    String password = ctx.formParam("password");
    boolean isAuthor = ctx.formParam("is_author") != null;
    Role role = isAuthor ? Role.AUTHOR : Role.USER;

    User user = userService.getUserByUserId(userId);
    if (user == null) {
      ctx.status(404).result("User not found");
      return;
    }
    user.setUsername(username);
    user.setName(name);
    user.setPassword(password);
    user.setRole(role);

    // Procesar la nueva foto si se ha subido
    if (ctx.uploadedFile("profilePhoto") != null) {
      try {
        var uploadedFile = ctx.uploadedFile("profilePhoto");
        byte[] bytes = uploadedFile.content().readAllBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        Photo newPhoto =
            new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
        user.setProfilePhoto(newPhoto);
        uploadedFile.content().close();
      } catch (IOException e) {
        e.printStackTrace();
        ctx.status(500).result("Error processing profile photo.");
        return;
      }
    }

    userService.updateUser(user);
    ctx.status(200).redirect(Routes.HOME.getPath());
    return;
  }

  public void deleteUser(Context ctx) {
    String username = ctx.pathParam("username");
    userService.deleteUserByUsername(username);
    ctx.status(200);
  }

  protected User processUserForm(Context ctx, String redirectRoute) {
    String name = ctx.formParam("name");
    String username = ctx.formParam("username");
    String password = ctx.formParam("password");
    boolean isAuthor = ctx.formParam("is_author") != null;

    if (name == null || username == null || password == null) {
      ctx.redirect(redirectRoute);
      return null;
    }

    try {
      userService.getUserByUsername(username);
      ctx.redirect(redirectRoute);
      return null;
    } catch (IllegalArgumentException e) {
      // Usuario no encontrado, se puede crear
    }

    Role role = isAuthor ? Role.AUTHOR : Role.USER;

    // Procesar la foto de perfil si se ha subido
    Photo profilePhoto = null;
    if (ctx.uploadedFile("profilePhoto") != null) {
      try {
        var uploadedFile = ctx.uploadedFile("profilePhoto");
        byte[] bytes = uploadedFile.content().readAllBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        profilePhoto =
            new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
      } catch (IOException e) {
        e.printStackTrace();
        ctx.status(500).result("Error processing profile photo.");
        return null;
      }
    }

    /*if (profilePhoto == null) {
        profilePhoto = new Photo("default.png", "image/png", getDefaultProfilePhotoBase64());
    }*/

    return userService.createUser(
        username, name, password, role, AccessStatus.UNAUTHENTICATED, profilePhoto);
  }

  /*public String getDefaultProfilePhotoBase64() {
      try {
          byte[] defaultImageBytes = Files.readAllBytes(Path.of("/public/profilepic.png"));
          return Base64.getEncoder().encodeToString(defaultImageBytes);
      } catch (IOException e) {
          e.printStackTrace();
          return ""; // Si hay un error, devolver una cadena vacía.
      }
  }*/

}
