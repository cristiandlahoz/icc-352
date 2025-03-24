package org.example.config;

import lombok.Getter;
import org.sql2o.Sql2o;

public class CoackroachDBConfig {
  @Getter
  private static Sql2o sql2o;

  static {
    String databaseUrl = EnvConfig.get("JDBC_DATABASE_URL");
    if (databaseUrl == null || databaseUrl.isEmpty()) {
      System.err.println("\u001B[31mWARNING:\u001B[0m JDBC_DATABASE_URL is not set. Defaulting to in-memory database.");
      databaseUrl = "jdbc:h2:mem:testdb";
    } else {
      System.out.println("\u001B[32mINFO:\u001B[0m JDBC_DATABASE_URL is set");
    }
    sql2o = new Sql2o(databaseUrl, "", "");
  }
}
