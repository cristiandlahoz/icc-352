package org.wornux.urlshortener.dao;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.base.BaseDAO;
import org.wornux.urlshortener.model.ShortenedUrl;

import dev.morphia.Datastore;

public class ShortenedUrlDAO extends BaseDAO<ShortenedUrl, ObjectId> {

  public ShortenedUrlDAO(Datastore datastore) {
    super(datastore, ShortenedUrl.class);
  }
}
