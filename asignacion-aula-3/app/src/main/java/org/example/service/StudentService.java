package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;


import java.util.List;
import java.util.Optional;

public class StudentService {

  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> getAllStudents() {
    return studentRepository.getAll();
  }

  public Optional<Student> getStudentByMatricula(int matricula) {
    if (matricula <= 0) {
      throw new IllegalArgumentException("Matricula must be greater than 0");
    }
    return studentRepository.findById(matricula);
  }

  public void createStudent(int matricula, String nombre, String carrera) {
    if (getStudentByMatricula(matricula).isPresent()) {
      throw new IllegalArgumentException("Student already exists");
    }
    if (nombre == null || nombre.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
    if (carrera == null || carrera.isEmpty()) {
      throw new IllegalArgumentException("Career cannot be empty");
    }
    if (matricula <= 0) {
      throw new IllegalArgumentException("Matricula must be greater than 0");
    }

    Student student = new Student(matricula, nombre, carrera);
    studentRepository.create(student);
  }

  public void updateStudent(int matricula, String nombre, String carrera) {
    Optional<Student> existingStudent = getStudentByMatricula(matricula);
    if (existingStudent.isEmpty()) {
      throw new IllegalArgumentException("Student does not exist");
    }
    if (nombre == null || nombre.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
    if (carrera == null || carrera.isEmpty()) {
      throw new IllegalArgumentException("Career cannot be empty");
    }

    Student student = existingStudent.get();
    student.setNombre(nombre);
    student.setCarrera(carrera);

    studentRepository.update(student);
  }

  public void deleteStudent(int matricula) {
    Optional<Student> existingStudent = getStudentByMatricula(matricula);
    if (existingStudent.isEmpty()) {
      throw new IllegalArgumentException("Student does not exist");
    }

    studentRepository.delete(matricula);
  }
}
