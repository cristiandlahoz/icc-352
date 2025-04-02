package org.wornux.urlshortener.util;

import java.util.UUID;

public class UrlShortener {

  public static String generateShortUrl(String originalUrl) {
    if (originalUrl == null || originalUrl.isBlank()) {
      throw new IllegalArgumentException("Original URL cannot be null or blank");
    }
    String shortUrl = UUID.randomUUID().toString().substring(0, 6);
    return shortUrl;
  }
}
