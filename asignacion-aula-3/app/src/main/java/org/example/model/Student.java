package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "matricula", nullable = false, unique = true)
  private int matricula;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "carrera", nullable = false, length = 100)
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
