package org.wornux.urlshortener.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.wornux.urlshortener.service.ShortUrlService;

public class ShortUrlController {
    public static void registerRoutes(Javalin app) {
        app.post("/shorten", ShortUrlController::shortenUrl);
    }

    private static void shortenUrl(Context ctx) {
        String originalUrl = ctx.formParam("url");
        if (originalUrl == null || originalUrl.isEmpty()) {
            ctx.status(400).result("URL is required");
            return;
        }
        var shortUrl = ShortUrlService.shortenUrl(originalUrl);
        ctx.json(shortUrl);
    }
}
