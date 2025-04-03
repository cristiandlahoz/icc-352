package org.wornux.urlshortener.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.ShortenedUrlDAO;
import org.wornux.urlshortener.dto.ShortenedUrlCreatedDTO;
import org.wornux.urlshortener.dto.ShortenedUrlDTO;
import org.wornux.urlshortener.model.ShortenedUrl;
import org.wornux.urlshortener.util.QRCodeGenerator;
import org.wornux.urlshortener.util.UrlShortener;

public class ShortenedUrlService {
  private final ShortenedUrlDAO shortenedUrlDAO;

  public ShortenedUrlService(ShortenedUrlDAO shortenedUrlDAO) {
    this.shortenedUrlDAO = shortenedUrlDAO;
  }

  public List<ShortenedUrl> getAllShortenedUrls() {
    return shortenedUrlDAO.findAll();
  }

  public void createShortenedUrl(ShortenedUrlDTO shortenedUrlDTO) {
    if (shortenedUrlDTO == null) {
      throw new IllegalArgumentException("Shortened URL cannot be null");
    }
    try {
      String originalUrl = shortenedUrlDTO.originalUrl();
      String shortUrl = UrlShortener.generateShortUrl(originalUrl);
      byte[] qrCode;
      qrCode = QRCodeGenerator.generateQRCode(originalUrl);
      ShortenedUrl shortenedUrl = new ShortenedUrl(originalUrl, shortUrl, shortenedUrlDTO.createdBy(), qrCode);
      shortenedUrlDAO.save(shortenedUrl);
    } catch (IOException e) {
      throw new RuntimeException("Failed to generate QR code", e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid URL format", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create shortened URL", e);
    }
  }

  public Optional<ShortenedUrlCreatedDTO> getShortenedUrlById(ObjectId id) {
    Optional<ShortenedUrl> shortenedUrl = shortenedUrlDAO.findById(id);
    if (shortenedUrl.isEmpty()) {
      throw new IllegalArgumentException("Shortened URL not found");
    }
    return Optional
        .ofNullable(new ShortenedUrlCreatedDTO(shortenedUrl.get()));
  }

  public void deleteShortenedUrl(ObjectId id) {
    shortenedUrlDAO.deleteById(id);
  }

}
