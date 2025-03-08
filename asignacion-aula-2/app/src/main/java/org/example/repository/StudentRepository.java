package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.model.Student;
import org.sql2o.*;

public class StudentRepository {
  private Sql2o sql2o;

  public StudentRepository(Sql2o sql2o) {
    this.sql2o = sql2o;
  }

  public List<Student> getAllStudents() {
    String sql = "SELECT * FROM estudiante";
    try (Connection conn = sql2o.open()) {
      return conn.createQuery(sql).executeAndFetch(Student.class);
    } catch (Exception e) {
      System.out.println("Error getting students" + e.getMessage());
      return null;
    }
  }

  public Optional<Student> getStudentByMatricula(int matricula) {
    String sql = "SELECT * FROM estudiante WHERE matricula = :matricula";
    try (Connection conn = sql2o.open()) {
      return Optional.ofNullable(conn.createQuery(sql)
          .addParameter("matricula", matricula)
          .executeAndFetchFirst(Student.class));
    } catch (Exception e) {
      System.out.println("Error getting student by matricula" + e.getMessage());
      return null;
    }
  }

  public void createStudent(Student student) {
    String sql = "INSERT INTO estudiante (matricula, nombre, carrera) VALUES (:matricula, :nombre, :carrera)";
    try (Connection conn = sql2o.open()) {
      conn.createQuery(sql)
          .addParameter("matricula", student.getMatricula())
          .addParameter("nombre", student.getNombre())
          .addParameter("carrera", student.getCarrera())
          .executeUpdate();
    } catch (Exception e) {
      System.out.println("Error creating new student" + e.getMessage());
    }
  }

  public void updateStudent(Student student) {
    String sql = "UPDATE estudiante SET nombre = :nombre, carrera = :carrera WHERE matricula = :matricula";
    try (Connection conn = sql2o.open()) {
      conn.createQuery(sql)
          .addParameter("nombre", student.getNombre())
          .addParameter("carrera", student.getCarrera())
          .addParameter("matricula", student.getMatricula())
          .executeUpdate();
    } catch (Exception e) {
      System.out.println("Error updating student" + e.getMessage());
    }
  }

  public void deleteStudent(int matricula) {
    String sql = "DELETE FROM estudiante WHERE matricula = :matricula";
    try (Connection conn = sql2o.open()) {
      conn.createQuery(sql)
          .addParameter("matricula", matricula)
          .executeUpdate();
    } catch (Exception e) {
      System.out.println("Error deleting student" + e.getMessage());
    }
  }
}
