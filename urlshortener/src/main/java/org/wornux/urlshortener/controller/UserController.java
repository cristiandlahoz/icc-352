package org.wornux.urlshortener.controller;

import javax.annotation.Nonnull;

import org.wornux.urlshortener.controller.base.BaseController;
import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
import org.wornux.urlshortener.service.UserService;

import io.javalin.http.Context;

/**
 * UserController is a controller class that handles HTTP requests related to
 * users.
 */
@CONTROLLER(path = "/users")
public class UserController extends BaseController {
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
