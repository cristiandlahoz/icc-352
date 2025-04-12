package org.wornux.urlshortener.api.rest;

import io.javalin.http.Context;
import io.javalin.router.JavalinDefaultRouting;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.dto.UrlStatsDTO;
import org.wornux.urlshortener.enums.Role;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.UrlService;
import org.wornux.urlshortener.service.UserService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;
    private final UserService userService;

    public void applyRoutes(JavalinDefaultRouting router) {
        router.get("/users/{userId}/urls", this::getUrlsByUser, Role.USER);
    }

    public void getUrlsByUser(@Nonnull Context ctx) {
        try {
            ObjectId userId = new ObjectId(ctx.pathParam("userId"));
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent()) {
                List<UrlStatsDTO> urls = urlService.getUrlsByUser(userOpt.get());
                ctx.json(urls);
            } else {
                ctx.status(404).result("Usuario no encontrado");
            }
        } catch (Exception e) {
            ctx.status(400).result("ID de usuario inválido");
        }
    }
}
