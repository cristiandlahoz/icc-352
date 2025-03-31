package org.wornux.urlshortener.model;

import java.util.Date;

import org.bson.types.ObjectId;
import dev.morphia.annotations.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity("shortened_urls")
@Indexes(@Index(fields = @Field("shortenedUrl")))
public class ShortenedUrl {
  @Id
  private ObjectId id;
  private String originalUrl;
  private String shortenedUrl;
  @Reference
  private User createdBy;
  private Date createdAt;
  private int clickCount;
  private boolean isOffensive;
  private String qrCode;

  public ShortenedUrl(String originalUrl, String shortenedUrl, User createdBy, String qrCode) {
    this.originalUrl = originalUrl;
    this.shortenedUrl = shortenedUrl;
    this.createdBy = createdBy;
    this.qrCode = qrCode;
    this.isOffensive = false;
  }

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = new Date();
    }
  }
}
