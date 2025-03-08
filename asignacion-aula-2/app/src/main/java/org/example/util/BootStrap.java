package org.example.util;

import org.h2.tools.Server;
import org.sql2o.Sql2o;

//import org.h2.tools.Server;

public class BootStrap {
  private static BootStrap INSTANCE;
  private Sql2o sql2o;

  private BootStrap() {
  }

  public static BootStrap getInstance() {
    if (INSTANCE == null)
      INSTANCE = new BootStrap();
    return INSTANCE;
  }

  public void initDatabase() {
    try {
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon",
          "-ifNotExists")
          .start();

      String status = Server.createWebServer("-trace", "-webPort",
          "0").start().getStatus();
      System.out.println("Status web:" + status);

      this.sql2o = new Sql2o("jdbc:h2:tcp://localhost/~/database", "sa", "");
      createTables();

    } catch (Exception e) {
      System.out.println("Error starting web server: " + e.getMessage());
    }
  }

  public void stopDatabase() {
    try {
      Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    } catch (Exception e) {
      System.out.println("Error stopping web server: " + e.getMessage());
    }
  }

  private void createTables() {
    String sql = """
        CREATE TABLE IF NOT EXISTS estudiante (
            id IDENTITY PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            edad INT NOT NULL,
            correo VARCHAR(150) UNIQUE NOT NULL);
        """;

    try (var con = sql2o.open()) {
      con.createQuery(sql).executeUpdate();
      System.out.println("Tabla 'estudiante' verificada/creada.");
    } catch (Exception e) {
      System.out.println("Error al crear la tabla: " + e.getMessage());
    }
  }

  public Sql2o getSql2o() {
    return this.sql2o;
  }
}
