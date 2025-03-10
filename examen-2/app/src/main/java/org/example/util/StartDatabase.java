package org.example.util;

import org.h2.tools.Server;

public class StartDatabase {
  private static StartDatabase INSTANCE;

  private StartDatabase() {}

  public static StartDatabase getInstance() {
    if (INSTANCE == null) INSTANCE = new StartDatabase();
    return INSTANCE;
  }

  public void initDatabase() {
    try {
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon", "-ifNotExists")
          .start();

      String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
      System.out.println("Status web:" + status);
    } catch (Exception e) {
      System.out.println("Error starting web server: " + e.getMessage());
    }
  }
}
