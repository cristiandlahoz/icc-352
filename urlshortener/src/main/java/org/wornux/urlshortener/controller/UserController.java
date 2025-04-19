package org.wornux.urlshortener.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
import org.wornux.urlshortener.enums.Role;
import org.wornux.urlshortener.enums.Routes;
import org.wornux.urlshortener.enums.SessionKeys;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.UserService;

/** UserController is a controller class that handles HTTP requests related to users. */
@CONTROLLER(path = "/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Handles the GET request to retrieve all users.
   *
   * @param ctx The context of the HTTP request.
   */
  @GET(path = "/")
  public void getUsers(@Nonnull Context ctx) {
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    if (user == null) {
      ctx.redirect(Routes.USER_LOGIN.getRoute());
      return;
    } else if (!user.getRole().equals(Role.ADMIN)) {
      ctx.status(HttpStatus.FORBIDDEN.getCode());
      return;
    }
    Map<String, Object> model =
        new HashMap<>() {
          {
            put("user", user);
            put("users", userService.getUsers());
          }
        };
    ctx.render("pages/users.html", model);
  }
}
