package org.wornux.urlshortener.dao;

import javax.annotation.Nonnull;

import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.base.BaseDAO;
import org.wornux.urlshortener.model.User;

import dev.morphia.Datastore;

public class UserDAO extends BaseDAO<User, ObjectId> {
  public UserDAO(@Nonnull Datastore datastore) {
    super(datastore, User.class);
  }
}
