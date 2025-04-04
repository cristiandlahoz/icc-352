package org.wornux.urlshortener.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.base.BaseDAO;
import org.wornux.urlshortener.model.AccessLog;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;

public class AccessLogsDAO extends BaseDAO<AccessLog, ObjectId> {

  public AccessLogsDAO(Datastore datastore) {
    super(datastore, AccessLog.class);
  }

  public List<AccessLog> findAccessLogsByShortenedUrlId(ObjectId shortenedUrlId) {
    return datastore.find(AccessLog.class)
        .filter(Filters.eq("shortenedUrl", shortenedUrlId))
        .iterator()
        .toList();
  }
}
