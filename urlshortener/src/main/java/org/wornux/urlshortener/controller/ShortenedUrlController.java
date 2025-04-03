package org.wornux.urlshortener.controller;

import io.javalin.http.Context;
import java.util.Base64;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
import org.wornux.urlshortener.core.routing.annotations.POST;
import org.wornux.urlshortener.dto.ShortenedUrlCreatedDTO;
import org.wornux.urlshortener.dto.ShortenedUrlDTO;
import org.wornux.urlshortener.enums.SessionKeys;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.ShortenedUrlService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CONTROLLER(path = "shortened")
public class ShortenedUrlController {

  private final ShortenedUrlService shortenedUrlService;

  public ShortenedUrlController(ShortenedUrlService shortenedUrlService) {
    this.shortenedUrlService = shortenedUrlService;
  }

  @GET(path = "/")
  public void listShortenedUrls(Context ctx) {
    Map<String, Object> model = new HashMap<>() {
      {
        put("urls", shortenedUrlService.getAllShortenedUrls());
      }
    };
    ctx.render("pages/home.html", model);
  }

  @POST(path = "/create")
  public void createShortenedUrl(Context ctx) {
    String url = ctx.formParam("url");
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    ShortenedUrlDTO shortenedUrlDTO = new ShortenedUrlDTO(url, user);
    shortenedUrlService.createShortenedUrl(shortenedUrlDTO);

    ctx.redirect("/");
  }

  @GET(path = "/{id}")
  public void getShortenedUrl(Context ctx) {
    String id = ctx.pathParam("id");

    Optional<ShortenedUrlCreatedDTO> url = shortenedUrlService.getShortenedUrlById(new ObjectId(id));
    Map<String, Object> model = new HashMap<>() {
      {
        put("shortenedUrl", url);
      }
    };
    ctx.render("shortenedUrlDetails.html", model);
  }

  @POST(path = "/{id}/delete")
  public void deleteShortenedUrl(Context ctx) {
    String id = ctx.pathParam("id");
    shortenedUrlService.deleteShortenedUrl(new ObjectId(id));
    ctx.redirect("/shortened/");
  }

  @GET(path = "/{id}/qr")
  public void getQrCode(Context ctx) {
    String id = ctx.pathParam("id");
    Optional<ShortenedUrlCreatedDTO> shortenedUrl = shortenedUrlService.getShortenedUrlById(new ObjectId(id));
    if (shortenedUrl.isEmpty()) {
      ctx.status(404);
      return;
    }
    String base64QRCode = Base64.getEncoder().encodeToString(shortenedUrl.get().qrCode());
    Map<String, Object> model = new HashMap<>() {
      {
        put("base64QRCode", base64QRCode);
      }
    };
    ctx.render("pages/qrCode.html", model);
  }
}
