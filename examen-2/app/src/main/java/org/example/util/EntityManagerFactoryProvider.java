package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryProvider {

  private static EntityManagerFactory emf;

  // Obtener una instancia de EntityManagerFactory
  public static EntityManagerFactory getEntityManagerFactory() {
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory("jpa-unit");
    }
    return emf;
  }

  // Crear un EntityManager
  public static EntityManager getEntityManager() {
    return getEntityManagerFactory().createEntityManager();
  }
}
