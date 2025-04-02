package org.wornux.urlshortener.controller;

import io.javalin.http.Context;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
import org.wornux.urlshortener.core.routing.annotations.POST;
import org.wornux.urlshortener.dto.ShortenedUrlCreatedDTO;
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
}
