package org.wornux.urlshortener.enums;

public enum Datastore {
  MONGO_DATABASE("urlshortener");

  private final String value;

  Datastore(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
