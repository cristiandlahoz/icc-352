package org.wornux.urlshortener.dao;

import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.base.BaseDAO;
import org.wornux.urlshortener.model.LinkPreview;

public class LinkPreviewDAO extends BaseDAO<LinkPreview, ObjectId> {

  public LinkPreviewDAO(Datastore datastore) {
    super(datastore, LinkPreview.class);
  }
}
