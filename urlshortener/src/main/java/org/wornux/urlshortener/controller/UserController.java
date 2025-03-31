package org.wornux.urlshortener.controller;

import javax.annotation.Nonnull;

import org.wornux.urlshortener.controller.base.BaseController;
import org.wornux.urlshortener.enums.Routes;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController extends BaseController {

  public UserController(Javalin app) {
    super(app);
  }

  @Override
  public void mapEndpoints() {
    app.get(Routes.USER_LIST.getRoute(), this::getUsers);
  }

  public void getUsers(@Nonnull Context ctx) {
    ctx.result("Users");
  }

}
