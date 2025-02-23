package org.example.repository;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class AuthRepository {
  private final Sql2o sql2o;

  public AuthRepository(Sql2o sql2o) {
    this.sql2o = sql2o;
    createTableIfNotExist();
  }

  public void logAuthentication(String username) {
    String sql = "INSERT INTO user_authentication_logs (username) VALUES (:username)";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql).addParameter("username", username).executeUpdate();
      System.out.println("Authentication log record created");
    } catch (Exception e) {
      System.out.println("Authentication log record could not be created: " + e.getMessage());
    }
  }

  private void createTableIfNotExist() {
    String createTableQuery =
        """
				    CREATE TABLE IF NOT EXISTS user_authentication_logs (
				        id SERIAL PRIMARY KEY,
				        username VARCHAR(255) NOT NULL,
				        login_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
				    );
				""";
    try (Connection con = sql2o.open()) {
      con.createQuery(createTableQuery).executeUpdate();
      System.out.println("Table 'user_authentication_logs' verified correctly");
    } catch (Exception e) {
      System.out.println(
          "Table 'user_authentication_logs' not verified correctly: " + e.getMessage());
    }
  }
}
