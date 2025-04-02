package org.wornux.urlshortener.security;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.wornux.urlshortener.enums.Role;

/**
 * Middleware for handling authentication and authorization in the application.
 * Ensures that the user is authenticated and has the required role to access
 * specific resources.
 */
public class AuthMiddleware implements Handler {

  /**
   * Handles the authentication middleware logic.
   * 
   * @param ctx The context of the current request, providing access to request
   *            and response data.
   * @throws Exception If an error occurs during the handling of the request.
   */
  @Override
  public void handle(Context ctx) throws Exception {
    if (ctx.path().equals("/"))
      ctx.redirect("shortened");

    if (ctx.sessionAttribute("user") == null) {
      ctx.status(401); // Unauthorized
      return;
    }

    // Retrieve the user's role from the session and check if they have admin
    // privileges.
    Role role = ctx.sessionAttribute("user");
    if (role != Role.ADMIN) {
      ctx.status(403); // Forbidden
      return;
    }
  }
}
