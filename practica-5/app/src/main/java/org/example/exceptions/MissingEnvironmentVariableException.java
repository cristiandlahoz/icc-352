package org.example.exceptions;

public class MissingEnvironmentVariableException extends RuntimeException {
  public MissingEnvironmentVariableException(String message) {
    super("Missing environment variable: " + message);
  }
}
