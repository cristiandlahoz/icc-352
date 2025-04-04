package org.wornux.urlshortener.controller;

import java.util.Optional;

import org.wornux.urlshortener.core.routing.annotations.CONTROLLER;
import org.wornux.urlshortener.core.routing.annotations.GET;
import org.wornux.urlshortener.dto.AccessLogDTO;
import org.wornux.urlshortener.dto.ShortenedUrlCreatedDTO;
import org.wornux.urlshortener.model.ShortenedUrl;
import org.wornux.urlshortener.service.AccessLogsService;
import org.wornux.urlshortener.service.ShortenedUrlService;
import org.wornux.urlshortener.util.UserAgentParser;

import io.javalin.http.Context;

@CONTROLLER(path = "/s")
public class RedirectController {
  private final ShortenedUrlService shortenedUrlService;
  private final AccessLogsService accessLogService;

  /**
   * Constructor to initialize the RedirectController with the required service.
   *
   * @param shortenedUrlService Service for managing shortened URLs.
   */
  public RedirectController(ShortenedUrlService shortenedUrlService, AccessLogsService accessLogService) {
    this.shortenedUrlService = shortenedUrlService;
    this.accessLogService = accessLogService;
  }

  /**
   * Handles GET requests to redirect to the original URL.
   *
   * @param ctx The Javalin HTTP context.
   */
  @GET(path = "/{shortUrl}")
  public void redirectToOriginalUrl(Context ctx) {
    String shortUrl = ctx.pathParam("shortUrl");
    Optional<ShortenedUrl> shortenedUrl = shortenedUrlService.getShortenedUrlByHash(shortUrl);
    if (shortenedUrl.isPresent()) {
      // Increment click count
      // take metrics
      String userAgent = ctx.userAgent();
      String ipAddress = ctx.ip();
      String browser = UserAgentParser.getBrowser(userAgent);
      String operatingSystem = UserAgentParser.getOperatingSystem(userAgent);

      AccessLogDTO accessLogDTO = new AccessLogDTO(
          shortenedUrl.get().getShortenedUrl(),
          shortenedUrl.get().getCreatedAt(),
          browser,
          ipAddress,
          operatingSystem);

      accessLogService.createAccessLog(accessLogDTO);
      ctx.redirect(shortenedUrl.get().getOriginalUrl());
    } else {
      ctx.status(404).result("Shortened URL not found");
    }
  }
}
