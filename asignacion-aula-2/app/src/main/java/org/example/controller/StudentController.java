package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.model.Student;
import org.example.service.StudentService;
import org.example.util.BaseController;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class StudentController extends BaseController {
  private StudentService studentService;

  public StudentController(StudentService studentService, Javalin app) {
    super(app);
    this.studentService = studentService;
  }

  @Override
  public void applyRoutes() {
    app.get("/students", this::getStudents);

  }

  public void getStudents(Context ctx) {
    List<Student> students = studentService.getAllStudents();
    Map<String, Object> model = setModel("titulo", "Student list", "lista", students);
    ctx.render("listar.html", model);
  }

}
