package org.wornux.urlshortener.util;

import java.util.UUID;

public class UrlShortener {
  public static String generateShortUrl(String originalUrl) {
    // Example logic for generating short URL
    return UUID.randomUUID().toString().substring(0, 6);
  }
}
