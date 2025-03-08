package org.example.service;

import java.util.List;
import java.util.Optional;

import org.example.model.Student;
import org.example.repository.StudentRepository;

public class StudentService {
  private StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> getAllStudents() {
    return studentRepository.getAllStudents();
  }

  public Optional<Student> getStudentByMatricula(int matricula) {
    if (matricula <= 0) {
      throw new IllegalArgumentException("Matricula must be greater than 0");
    } else if (getStudentByMatricula(matricula).isEmpty()) {
      throw new IllegalArgumentException("Student does not exist");
    } else {
      return studentRepository.getStudentByMatricula(matricula);
    }
  }

  public void createStudent(int matricula, String nombre, String carrera) {
    if (getStudentByMatricula(matricula).isPresent()) {
      throw new IllegalArgumentException("Student already exists");
    } else if (nombre == null || nombre.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    } else if (carrera == null || carrera.isEmpty()) {
      throw new IllegalArgumentException("Career cannot be empty");
    } else if (matricula <= 0) {
      throw new IllegalArgumentException("Matricula must be greater than 0");
    } else {
      Student student = new Student(matricula, nombre, carrera);
      studentRepository.createStudent(student);
    }

  }

  public void updateStudent(int matricula, String nombre, String carrera) {
    if (getStudentByMatricula(matricula).isEmpty()) {
      throw new IllegalArgumentException("Student does not exist");
    } else if (matricula <= 0) {
      throw new IllegalArgumentException("Matricula must be greater than 0");
    } else if (nombre == null || nombre.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    } else if (carrera == null || carrera.isEmpty()) {
      throw new IllegalArgumentException("Career cannot be empty");
    } else {
      Student student = new Student(matricula, nombre, carrera);
      studentRepository.updateStudent(student);
    }
  }

  public void deleteStudent(int matricula) {
    if (getStudentByMatricula(matricula).isEmpty()) {
      throw new IllegalArgumentException("Student does not exist");
    } else if (matricula <= 0) {
      throw new IllegalArgumentException("Matricula must be greater than 0");
    } else {
      studentRepository.deleteStudent(matricula);
    }
  }

}
