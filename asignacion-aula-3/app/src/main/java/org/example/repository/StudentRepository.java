package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Student;
import org.example.util.BaseRepository;

import java.util.Optional;

public class StudentRepository extends BaseRepository<Student, Integer> {

  public StudentRepository(EntityManager entityManager) {
    super(entityManager, Student.class);
  }

}
