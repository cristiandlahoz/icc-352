package org.wornux.urlshortener.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.UrlDAO;
import org.wornux.urlshortener.dto.UrlCreatedDTO;
import org.wornux.urlshortener.dto.UrlDTO;
import org.wornux.urlshortener.model.Url;
import org.wornux.urlshortener.util.QRCodeGenerator;
import org.wornux.urlshortener.util.UrlShortener;

public class UrlService {
  private final UrlDAO urlDAO;

  public UrlService(UrlDAO urlDAO) {
    this.urlDAO = urlDAO;
  }

  public List<UrlCreatedDTO> getAllShortenedUrls() {
    return urlDAO.findAll().stream().map(UrlCreatedDTO::new).toList();
  }

  public Optional<UrlCreatedDTO> getById(ObjectId id) {
    return urlDAO.findById(id).map(UrlCreatedDTO::new);
  }

  public void createShortenedUrl(UrlDTO urlDTO) {
    if (urlDTO == null) {
      throw new IllegalArgumentException("Shortened URL cannot be null");
    }
    try {
      String originalUrl = urlDTO.originalUrl();
      String shortUrl = UrlShortener.generateShortUrl();
      byte[] qrCode;
      qrCode = QRCodeGenerator.generateQRCode(originalUrl);
      Url url = new Url(originalUrl, shortUrl, urlDTO.createdBy(), qrCode);
      urlDAO.save(url);
    } catch (IOException e) {
      throw new RuntimeException("Failed to generate QR code", e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid URL format", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create shortened URL", e);
    }
  }

  public Optional<UrlCreatedDTO> getShortenedUrlById(ObjectId id) {
    Optional<Url> shortenedUrl = urlDAO.findById(id);
    if (shortenedUrl.isEmpty()) {
      throw new IllegalArgumentException("Shortened URL not found");
    }
    return Optional.ofNullable(new UrlCreatedDTO(shortenedUrl.get()));
  }

  public void deleteShortenedUrl(ObjectId id) {
    urlDAO.deleteById(id);
  }

  public Optional<Url> getShortenedUrlByHash(String shortUrl) {
    Optional<Url> shortenedUrl = urlDAO.findByHash(shortUrl);
    if (shortenedUrl.isEmpty()) {
      throw new IllegalArgumentException("Shortened URL not found");
    }
    return shortenedUrl;
  }

  public void incrementClickCount(ObjectId id) {
    Optional<Url> shortenedUrl = urlDAO.findById(id);
    if (shortenedUrl.isPresent()) {
      Url url = shortenedUrl.get();
      url.setClickCount(url.getClickCount() + 1);
      urlDAO.save(url);
    } else {
      throw new IllegalArgumentException("Shortened URL not found");
    }
  }
}
