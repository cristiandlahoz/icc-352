package org.wornux.urlshortener.api.rest.security;

import io.javalin.http.Context;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;
import org.wornux.urlshortener.config.DependencyConfig;
import org.wornux.urlshortener.enums.Role;
import org.wornux.urlshortener.model.User;

import java.util.List;
import java.util.Optional;

public class AuthMiddleware {

  private static final JwtUtil jwtUtil = new JwtUtil();

  public static List<Role> getUserRoles(Context ctx) {
    String authHeader = ctx.header(Header.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return List.of(); // no autorizado
    }

    String token = authHeader.substring(7); // quitar "Bearer "

    if (!jwtUtil.validateToken(token)) {
      return List.of(); // token inválido
    }

    String username = jwtUtil.getUsernameFromToken(token);

    Optional<User> userOpt = DependencyConfig.getUserService().getUserByUsername(username);

    return userOpt.map(user -> List.of(user.getRole())).orElse(List.of());
  }

  public static void handleAccess(Context ctx) {
    var permittedRoles = ctx.routeRoles();

    if (permittedRoles.contains(Role.ANYONE)) return;

    if (getUserRoles(ctx).stream().anyMatch(permittedRoles::contains)) return;

    ctx.header(Header.WWW_AUTHENTICATE, "Bearer");
    throw new UnauthorizedResponse("No autorizado");
  }
}
