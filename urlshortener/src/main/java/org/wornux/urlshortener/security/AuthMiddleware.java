package org.wornux.urlshortener.security;

/**
 * Middleware for handling authentication in the URL shortener application. Ensures that users are
 * authenticated before accessing protected routes.
 */
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.wornux.urlshortener.enums.Routes;
import org.wornux.urlshortener.enums.SessionKeys;
import org.wornux.urlshortener.model.User;

/**
 * AuthMiddleware class implements the Handler interface to provide authentication checks for
 * incoming HTTP requests.
 */
public class AuthMiddleware implements Handler {

  /**
   * Handles incoming HTTP requests and performs authentication checks. Redirects unauthenticated
   * users to the login page for protected routes.
   *
   * @param ctx The Javalin HTTP context for the current request.
   * @throws Exception If an error occurs during request handling.
   */
  @Override
  public void handle(Context ctx) throws Exception {
    String currentPath = ctx.path();

    // Allow public and authentication routes without checks
    if (currentPath.startsWith("/images")
        || currentPath.equals("/shortened")
        || currentPath.startsWith("/css")
        || currentPath.startsWith("/js")
        || currentPath.startsWith("/auth")) return;

    // Check if the user is authenticated
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    if (user == null) ctx.redirect(Routes.USER_LOGIN.getRoute());
  }
}
