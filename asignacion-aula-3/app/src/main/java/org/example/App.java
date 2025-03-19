package org.example;

import io.javalin.Javalin;
import org.example.config.AppConfig;
import org.example.config.DependencyConfig;
import org.example.controller.StudentController;
import org.example.util.StartDatabase;

import java.util.List;

public class App {
  public static void main(String[] args) {
    int port = 8080;
    Javalin app = AppConfig.createApp().start(port);

    StartDatabase.getInstance().initDatabase();
    DependencyConfig.init();

    List.of(new StudentController(DependencyConfig.getStudentService(), app))
            .forEach(StudentController::applyRoutes);

  }
}
