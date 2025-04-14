package org.wornux.urlshortener.controller;

import io.javalin.http.Context;
import javax.annotation.Nonnull;
import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
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
    ctx.result("Users");
  }
}
