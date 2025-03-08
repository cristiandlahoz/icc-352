package org.example.model;

import java.util.Objects;

public class Student {
  private int matricula;
  private String nombre;
  private String carrera;

  public Student() {
  }

  public Student(int matricula, String nombre, String carrera) {
    this.matricula = matricula;
    this.nombre = nombre;
    this.carrera = carrera;
  }

  public int getMatricula() {
    return matricula;
  }

  public void setMatricula(int matricula) {
    this.matricula = matricula;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCarrera() {
    return carrera;
  }

  public void setCarrera(String carrera) {
    this.carrera = carrera;
  }

  public void mezclar(Student e) {
    matricula = e.getMatricula();
    nombre = e.getNombre();
    carrera = e.getCarrera();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Student that = (Student) o;
    return matricula == that.matricula;
  }

  @Override
  public int hashCode() {
    return Objects.hash(matricula);
  }

  @Override
  public String toString() {
    return "Estudiante{" +
        "matricula=" + matricula +
        ", nombre='" + nombre + '\'' +
        ", carrera='" + carrera + '\'' +
        '}';
  }
}
