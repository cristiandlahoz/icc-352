package org.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.example.repository.*;
import org.example.service.*;

public class DependencyConfig {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    private static StudentRepository studentRepository;

    @Getter private static StudentService studentService;

    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("sqlite-persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();

        studentRepository = new StudentRepository(entityManager);
        studentService = new StudentService(studentRepository);
    }

    public static void close() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
