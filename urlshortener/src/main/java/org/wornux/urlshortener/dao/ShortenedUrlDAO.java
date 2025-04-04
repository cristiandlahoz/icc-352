package org.wornux.urlshortener.dao;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.base.BaseDAO;
import org.wornux.urlshortener.model.ShortenedUrl;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;

public class ShortenedUrlDAO extends BaseDAO<ShortenedUrl, ObjectId> {

  public ShortenedUrlDAO(Datastore datastore) {
    super(datastore, ShortenedUrl.class);
  }

  public Optional<ShortenedUrl> findByHash(String shortenedUrl) {
    return Optional.ofNullable(datastore.find(ShortenedUrl.class)
        .filter(Filters.eq("shortenedUrl", shortenedUrl))
        .first());
  }
}
