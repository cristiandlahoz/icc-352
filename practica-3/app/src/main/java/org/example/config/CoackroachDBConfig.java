package org.example.config;

import lombok.Getter;
import org.sql2o.Sql2o;

public class CoackroachDBConfig {
  @Getter
  private static Sql2o sql2o;

  static {
    String froomEnvironment = System.getenv("JDBC_DATABASE_URL");
    String databaseUrl = EnvConfig.get("JDBC_DATABASE_URL", froomEnvironment);
    if (databaseUrl == null || databaseUrl.isEmpty()) {
      throw new RuntimeException("JDBC_DATABASE_URL not set");
    }
    sql2o = new Sql2o(databaseUrl, "", "");
  }
}
