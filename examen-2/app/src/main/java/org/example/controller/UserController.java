package org.example.controller;

import io.javalin.http.Context;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Delete;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;
import org.example.util.enums.Routes;
import java.util.*;

@Controller(path = "/users")
public class UserController {
  UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @Get(path = "/manageusers")
  public void renderManageUsers(Context ctx) {
    ctx.render("/pages/manage_users.html");
  }

  @Get(path = "/")
  public void getAllUsers(Context ctx) {
    List<User> myUsers = userService.getAllUsers();
    // System.out.println("Usuarios encontrados: " + myUsers.size());
    ctx.json(myUsers);
  }

  @Get(path = "/user/{username}")
  public void getUserByUsername(Context ctx) {
    String username = ctx.pathParam("username");
    User myUser = userService.getUserByUsername(username).orElse(null);
    ctx.json(myUser);
  }

  @Post(path = "/{username}")
  public void updateUser(Context ctx) {
    ctx.attribute(
        "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));
    try {
      String username = ctx.pathParam("username");
      Optional<User> userOptional = userService.getUserByUsername(username);
      if (userOptional.isPresent()) {
        User existingUser = userOptional.get();
        String role = existingUser.getRole().toString();
        Map<String, Object> model = new HashMap<>();
        model.put("user", existingUser);
        model.put("role", role);
        ctx.render("/pages/update_user.html", model);
      } else {
        ctx.status(400).result("Error Updating User");
      }
    } catch (Exception e) {
      ctx.status(500).result("Error Updating User: " + e.getMessage());
    }
  }

  @Post(path = "/form/{id}")
  public void formHandler(Context ctx) {
    Long userId = Long.parseLong(ctx.pathParam("id"));

    Optional<User> userOptional = userService.getUserById(userId);
    if (userOptional.isEmpty()) {
      System.out.println("❌ Usuario no encontrado en la base de datos.");
      ctx.status(404).result("User not found");
      return;
    }

    // User user = userOptional.get(); // ✅ Se busca por ID para evitar conflictos
    String name = ctx.formParam("name");
    String newUsername = ctx.formParam("username"); // 🔄 Nuevo username para actualización
    String password = ctx.formParam("password");

    try {
      userService.updateUserById(userId, newUsername, password, name); // 🔥 Nuevo método por ID
      System.out.println("✅ Usuario actualizado con éxito.");
      ctx.status(200).redirect(Routes.HOME.getPath());
    } catch (IllegalArgumentException e) {
      ctx.status(400).result(e.getMessage());
    }
  }

  @Delete(path = "/{username}")
  public void deleteUser(Context ctx) {
    String username = ctx.pathParam("username");
    userService.deleteUser(username);
    ctx.status(200);
  }
}
