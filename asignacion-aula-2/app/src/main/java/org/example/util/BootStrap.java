package org.example.util;

import org.h2.tools.Server;
import org.sql2o.Sql2o;

public class BootStrap {
  private static BootStrap INSTANCE;
  private Sql2o sql2o;
  private Server tcpServer;
  private Server webServer;

  private BootStrap() {
  }

  public static BootStrap getInstance() {
    if (INSTANCE == null)
      INSTANCE = new BootStrap();
    return INSTANCE;
  }

  public void initDatabase() {
    try {
      // Inicia el servidor TCP
      tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
      webServer = Server.createWebServer("-trace", "-webPort", "8082").start();

      System.out.println("H2 Web Console running at: http://localhost:8082");

      // Conectar a H2 en modo TCP
      this.sql2o = new Sql2o("jdbc:h2:tcp://localhost/./database", "sa", "");
      createTables();

    } catch (Exception e) {
      System.out.println("Error iniciando la base de datos: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void stopDatabase() {
    try {
      if (tcpServer != null) {
        tcpServer.stop();
        System.out.println("Servidor TCP detenido.");
      }
      if (webServer != null) {
        webServer.stop();
        System.out.println("Consola Web detenida.");
      }
    } catch (Exception e) {
      System.out.println("Error deteniendo la base de datos: " + e.getMessage());
    }
  }

  private void createTables() {
    String sql = """
        CREATE TABLE IF NOT EXISTS estudiante (
            matricula IDENTITY PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            carrera VARCHAR(150) NOT NULL
        );
        """;

    try (var con = sql2o.open()) {
      con.createQuery(sql).executeUpdate();
      System.out.println("Tabla 'estudiante' creada/verificada.");
    } catch (Exception e) {
      System.out.println("Error creando la tabla: " + e.getMessage());
    }
  }

  public Sql2o getSql2o() {
    return this.sql2o;
  }
}
