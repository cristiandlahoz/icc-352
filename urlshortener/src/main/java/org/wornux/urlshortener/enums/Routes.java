package org.wornux.urlshortener.enums;

public enum Routes {
  // User routes
  USER_LOGIN("/login"),
  USER_REGISTER("/register"),
  USER_LOGOUT("/logout"),
  USER_PROFILE("/profile"),
  USER_UPDATE_PROFILE("/profile/update"),
  USER_DELETE_PROFILE("/profile/delete"),
  USER_LIST("/users"),
  USER_VIEW("/users/{id}"),
  USER_UPDATE("/users/{id}/update"),
  USER_DELETE("/users/{id}/delete"),
  // URL routes
  URL_LIST("/urls"),
  URL_CREATE("/urls/create"),
  URL_VIEW("/urls/{id}"),
  URL_UPDATE("/urls/{id}/update"),
  URL_DELETE("/urls/{id}/delete"),
  // Redirect routes
  REDIRECT("/r/{id}");

  private final String route;

  Routes(String route) {
    this.route = route;
  }

  public String getRoute() {
    return route;
  }
}
