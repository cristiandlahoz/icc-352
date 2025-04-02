package org.wornux.urlshortener.security;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.wornux.urlshortener.enums.Role;

public class AuthMiddleware implements Handler {

  @Override
  public void handle(Context ctx) throws Exception {
    var permittedRoles = ctx.routeRoles();
    // Check if the user is authenticated
    if (ctx.basicAuthCredentials() != null) {
      // Check if the user has the required role
      if (permittedRoles.contains(Role.ADMIN)) {
        // User is authenticated and has the required role
      } else {
        // User is authenticated but does not have the required role
        ctx.status(403).result("Forbidden");
      }
    } else {
      // User is not authenticated
      ctx.status(401).result("Unauthorized");
    }
  }
}
