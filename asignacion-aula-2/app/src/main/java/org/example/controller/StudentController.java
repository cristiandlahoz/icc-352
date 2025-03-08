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
    app.get("/", ctx -> ctx.redirect("/students"));
    app.get("/students", this::getStudents);
    app.get("/create", this::createStudentForm);
    app.post("/create", this::createStudent);
    app.get("/delete/{matricula}", this::deleteStudent);

  }

  public void getStudents(Context ctx) {
    List<Student> students = studentService.getAllStudents();
    Map<String, Object> model = setModel("titulo", "Student list", "lista", students);
    ctx.render("listar.html", model);
  }

  public void createStudentForm(Context ctx) {
    Map<String, Object> model = setModel("titulo", "Create student", "accion", "/create");
    ctx.render("crearEditarVisualizar.html", model);
  }

  public void createStudent(Context ctx) {
    int matricula = Integer.parseInt(ctx.formParam("matricula"));
    String name = ctx.formParam("nombre");
    String carrera = ctx.formParam("carrera");
    try {
      studentService.createStudent(matricula, name, carrera);
    } catch (Exception e) {
      System.out.println("Error creating student: " + e.getMessage());
    }
    ctx.redirect("/students");
  }

  public void deleteStudent(Context ctx) {
    int matricula = Integer.parseInt(ctx.pathParam("matricula"));
    try {
      studentService.deleteStudent(matricula);
    } catch (Exception e) {
      System.out.println("Error deleting student: " + e.getMessage());
    }
    ctx.redirect("/students");
  }

}
