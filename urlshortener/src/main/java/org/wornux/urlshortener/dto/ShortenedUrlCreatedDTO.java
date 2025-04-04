package org.wornux.urlshortener.dto;

import java.util.Date;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.model.ShortenedUrl;

/**
 * A Data Transfer Object (DTO) representing the creation of a shortened URL. This record
 * encapsulates the original URL, the shortened URL, and a QR code.
 *
 * @param originalUrl The original URL to be shortened. Must be a valid URL format.
 * @param shortenedUrl The shortened version of the URL. Must not exceed 10 characters.
 * @param qrCode The QR code representation of the shortened URL. Cannot be blank.
 * @throws IllegalArgumentException if any of the parameters are invalid.
 */
public record ShortenedUrlCreatedDTO(
    ObjectId id,
    String originalUrl,
    String shortenedUrl,
    Date createdAt,
    byte[] qrCode,
    int clickCount,
    boolean isOffensive) {

  /**
   * Constructs a new {@code ShortenedUrlCreatedDTO} instance with validation.
   *
   * @param originalUrl The original URL to be shortened.
   * @param shortenedUrl The shortened version of the URL.
   * @param qrCode The QR code representation of the shortened URL.
   * @throws IllegalArgumentException if: - {@code originalUrl} is null, blank, or not a valid URL
   *     format. - {@code shortenedUrl} is null, blank, or exceeds 10 characters. - {@code qrCode}
   *     is null or blank.
   */
  public ShortenedUrlCreatedDTO {
    if (originalUrl == null || originalUrl.isBlank()) {
      throw new IllegalArgumentException("Original URL cannot be null or blank");
    }
    if (!originalUrl.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
      throw new IllegalArgumentException(
          "Original URL must be a valid URL format (e.g., http://example.com)");
    }
    if (shortenedUrl == null || shortenedUrl.isBlank()) {
      throw new IllegalArgumentException("Shortened URL cannot be null or blank");
    }
    if (shortenedUrl.length() > 10) {
      throw new IllegalArgumentException("Shortened URL must not exceed 10 characters");
    }
    if (qrCode == null || qrCode.toString().isBlank()) {
      throw new IllegalArgumentException("");
    }
    if (clickCount < 0) {
      throw new IllegalArgumentException("Click count cannot be negative");
    }
  }

  /**
   * Constructs a ShortenedUrlCreatedDTO object using a ShortenedUrl instance.
   *
   * @param shortenedUrl The ShortenedUrl instance containing the data.
   */
  public ShortenedUrlCreatedDTO(ShortenedUrl shortenedUrl) {
    this(
        shortenedUrl.getId(),
        shortenedUrl.getOriginalUrl(),
        shortenedUrl.getShortenedUrl(),
        shortenedUrl.getCreatedAt(),
        shortenedUrl.getQrCode(),
        shortenedUrl.getClickCount(),
        shortenedUrl.isOffensive());
  }
}
