package org.example;

import org.example.controller.StudentController;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.example.util.BootStrap;
import org.sql2o.Sql2o;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class App {
  public static void main(String[] args) {
    int PORT = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 7000;
    Javalin app = Javalin.create(config -> {
      config.staticFiles.add(cf -> {
        cf.hostedPath = "/";
        cf.directory = "/public";
      });
      ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
      templateResolver.setPrefix("/templates/");

      TemplateEngine templateEngine = new TemplateEngine();
      templateEngine.setTemplateResolver(templateResolver);

      JavalinThymeleaf thymeleafRenderer = new JavalinThymeleaf(templateEngine);
      config.fileRenderer(thymeleafRenderer);
    }).start(PORT);

    BootStrap.getInstance().initDatabase();
    Sql2o sql2o = BootStrap.getInstance().getSql2o();
    StudentRepository studentRepository = new StudentRepository(sql2o);
    StudentService studentService = new StudentService(studentRepository);
    new StudentController(studentService, app).applyRoutes();
  }
}
