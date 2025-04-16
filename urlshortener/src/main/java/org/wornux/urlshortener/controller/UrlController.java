package org.wornux.urlshortener.controller;

import io.javalin.http.Context;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
import org.wornux.urlshortener.core.routing.annotations.POST;
import org.wornux.urlshortener.dto.UrlCreatedDTO;
import org.wornux.urlshortener.dto.UrlDTO;
import org.wornux.urlshortener.enums.SessionKeys;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.UrlService;

/** Controller for handling operations related to shortened URLs. */
@CONTROLLER(path = "shortened")
public class UrlController {

  private final UrlService urlService;

  /**
   * Constructor to initialize the UrlController with the required service.
   *
   * @param urlService Service for managing shortened URLs.
   */
  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  /**
   * Handles GET requests to list all shortened URLs.
   *
   * @param ctx The Javalin HTTP context.
   */
  @GET(path = "/")
  public void listShortenedUrls(Context ctx) {
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    if (user != null) {
      ctx.redirect("/shortened/logged");
      return;
    }
    Map<String, Object> model =
        new HashMap<>() {
          {
            put("user", user);
            put("urls", urlService.getShortenedUrlsByUser(user));
          }
        };
    ctx.render("pages/home.html", model);
  }

  /**
   * Handles GET requests to display the dashboard for logged-in users.
   *
   * @param ctx The Javalin HTTP context.
   */
  @GET(path = "/logged")
  public void logged(Context ctx) {
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    if (user == null) {
      ctx.redirect("/");
      return;
    }
    Map<String, Object> model =
        new HashMap<>() {
          {
            put("user", user);
            put("urls", urlService.getShortenedUrlsByUser(user));
          }
        };
    ctx.render("pages/registered.html", model);
  }

  /**
   * Handles GET requests to display the dashboard for a specific shortened URL.
   *
   * @param ctx The Javalin HTTP context.
   */
  @GET(path = "/{id}/dashboard")
  public void dashboard(Context ctx) {
    String id = ctx.pathParam("id");

    Optional<UrlCreatedDTO> url = urlService.getShortenedUrlById(new ObjectId(id));
    Map<String, Object> model =
        new HashMap<>() {
          {
            put("shortenedUrl", url);
            put("session", ctx.sessionAttribute(SessionKeys.USER.getKey()));
          }
        };
    ctx.render("pages/dashboard.html", model);
  }

  /**
   * Handles POST requests to create a new shortened URL.
   *
   * @param ctx The Javalin HTTP context.
   */
  @POST(path = "/create")
  public void createShortenedUrl(Context ctx) {
    String url = ctx.formParam("url");
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    UrlDTO urlDTO = new UrlDTO(url, user);
    urlService.createShortenedUrl(urlDTO);

    ctx.redirect("/");
  }

  /**
   * Handles GET requests to retrieve details of a specific shortened URL.
   *
   * @param ctx The Javalin HTTP context.
   */
  @GET(path = "/{id}")
  public void getShortenedUrl(Context ctx) {
    String id = ctx.pathParam("id");

    Optional<UrlCreatedDTO> url = urlService.getShortenedUrlById(new ObjectId(id));
    Map<String, Object> model =
        new HashMap<>() {
          {
            put("shortenedUrl", url);
          }
        };
    ctx.render("shortenedUrlDetails.html", model);
  }

  /**
   * Handles POST requests to delete a specific shortened URL.
   *
   * @param ctx The Javalin HTTP context.
   */
  @POST(path = "/{id}/delete")
  public void deleteShortenedUrl(Context ctx) {
    String id = ctx.pathParam("id");
    urlService.deleteShortenedUrl(new ObjectId(id));
    ctx.redirect("/shortened/");
  }

  /**
   * Handles GET requests to retrieve the QR code for a specific shortened URL.
   *
   * @param ctx The Javalin HTTP context.
   */
  @GET(path = "/{id}/qr")
  public void getQrCode(Context ctx) {
    String id = ctx.pathParam("id");
    Optional<UrlCreatedDTO> shortenedUrl = urlService.getShortenedUrlById(new ObjectId(id));
    if (shortenedUrl.isEmpty()) {
      ctx.status(404);
      return;
    }
    String base64QRCode = Base64.getEncoder().encodeToString(shortenedUrl.get().qrCode());
    Map<String, Object> model =
        new HashMap<>() {
          {
            put("base64QRCode", base64QRCode);
          }
        };
    ctx.render("pages/qrCode.html", model);
  }
}
