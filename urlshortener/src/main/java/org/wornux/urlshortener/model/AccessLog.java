package org.wornux.urlshortener.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Reference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity("access_logs")
@Getter
@Setter
@NoArgsConstructor
public class AccessLog {
  @Id
  private ObjectId id;
  @Reference
  private ShortenedUrl shortenedUrl;
  private Date accessDate;
  private String browser;
  private String ipAddress;
  private String platform;
  private String os; // Operating System

  public AccessLog(ShortenedUrl shortenedUrl, String browser, String ipAddress, String platform,
      String os) {
    this.shortenedUrl = shortenedUrl;
    this.browser = browser;
    this.ipAddress = ipAddress;
    this.platform = platform;
    this.os = os;
  }

  @PrePersist
  public void prePersist() {
    if (accessDate == null) {
      accessDate = new Date();
    }
  }
}
