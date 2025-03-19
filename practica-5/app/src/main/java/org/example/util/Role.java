package org.example.util;

import java.util.EnumSet;
import java.util.Set;

public enum Role {
  ADMIN(
      EnumSet.of(
          Permissions.ARTICLE_CREATE,
          Permissions.ARTICLE_EDIT,
          Permissions.ARTICLE_DELETE,
          Permissions.COMMENT_CREATE,
          Permissions.COMMENT_EDIT,
          Permissions.COMMENT_DELETE,
          Permissions.MANAGE_USERS)),
  AUTHOR(
      EnumSet.of(
          Permissions.ARTICLE_CREATE,
          Permissions.ARTICLE_EDIT,
          Permissions.COMMENT_CREATE,
          Permissions.COMMENT_EDIT)),
  USER(EnumSet.of(Permissions.COMMENT_CREATE, Permissions.COMMENT_EDIT)),
  GUEST(EnumSet.noneOf(Permissions.class)); // No tiene permisos

  private final Set<Permissions> permissions;

  Role(Set<Permissions> permissions) {
    this.permissions = permissions;
  }

  public boolean hasPermission(Permissions permission) {
    return permissions.contains(permission);
  }

  public Set<Permissions> getPermissions() {
    return permissions;
  }
}
