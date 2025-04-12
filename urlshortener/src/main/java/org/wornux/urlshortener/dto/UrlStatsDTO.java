package org.wornux.urlshortener.dto;

import java.util.Date;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.model.Url;

public record UrlStatsDTO(
        ObjectId id,
        String originalUrl,
        String shortenedUrl,
        Date createdAt,
        int clickCount
) {
    public UrlStatsDTO(Url url) {
        this(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortenedUrl(),
                url.getCreatedAt(),
                url.getClickCount()
        );
    }
}
