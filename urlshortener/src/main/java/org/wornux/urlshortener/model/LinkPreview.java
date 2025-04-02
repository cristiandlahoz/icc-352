package org.wornux.urlshortener.model;

import dev.morphia.annotations.*;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Entity("link_previews")
@Getter
@Setter
@NoArgsConstructor
public class LinkPreview {
  @Id private ObjectId id;
  @Reference private ShortenedUrl shortenedUrl;
  private String previewImage;
  private Date createdAt;

  public LinkPreview(ShortenedUrl shortenedUrl, String previewImage) {
    this.shortenedUrl = shortenedUrl;
    this.previewImage = previewImage;
  }

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = new Date();
    }
  }
}
