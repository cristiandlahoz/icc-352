package org.wornux.urlshortener.service;

import java.util.Date;
import java.util.UUID;
import org.wornux.urlshortener.model.ShortUrl;
import org.wornux.urlshortener.repository.ShortUrlRepository;

public class ShortUrlService {
  public static ShortUrl shortenUrl(String originalUrl) {
    String shortUrl = UUID.randomUUID().toString().substring(0, 6); // Simple shortener for demo
    ShortUrl newShortUrl = new ShortUrl();
    newShortUrl.setOriginalUrl(originalUrl);
    newShortUrl.setShortUrl(shortUrl);
    newShortUrl.setCreatedAt(new Date());

    ShortUrlRepository.saveShortUrl(newShortUrl);
    return newShortUrl;
  }
}
