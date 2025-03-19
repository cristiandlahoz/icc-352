package org.example.util;

public enum Routes {
  HOME("/"),
  LOGIN("/login"),
  LOGOUT("/logout"),
  SIGNUP("/signup"),
  CREATEARTICLE("/createarticle"),
  ARTICLES("/articles"),
  ARTICLE("/articles/{id}"),
  USERS("/users"),
  USER("/users/{username}"),
  CREATEUSER("/createuser"),
  COMMENTS("/comments"),
  COMMENT("/comments/{id}"),
  TAGS("/tags"),
  MANAGEUSERS("/manageusers"),
  PHOTOLIST("photolist");

  private final String path;

  Routes(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
