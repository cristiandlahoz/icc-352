package org.example.util.enums;

public enum Routes {
  HOME("/"),
  LOGIN("/login"),
  LOGOUT("/logout"),
  SIGNUP("/signup"),
  USERS("/users"),
  USER("/users/{username}");

  private final String path;

  Routes(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
