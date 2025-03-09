package org.example.util.baseclasses;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.*;

public class BaseRepository<T, ID> {
  protected EntityManager entityManager;
  protected final Class<T> entityClass;

  public BaseRepository(EntityManager entityManager, Class<T> entityClass) {
    this.entityManager = entityManager;
    this.entityClass = entityClass;
  }

  public Optional<T> findById(ID id) {
    return Optional.ofNullable(entityManager.find(entityClass, id));
  }

  public List<T> findAll() {
    return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
        .getResultList();
  }

  public T save(T entity) {
    EntityTransaction transaction = entityManager.getTransaction();

    try {
      transaction.begin();
      entityManager.persist(entity);
      transaction.commit();
      return entity;
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException("Error saving entitiy:" + entityClass.getSimpleName(), e);
    }
  }

  public T update(T entity) {
    EntityTransaction transaction = entityManager.getTransaction();

    try {
      transaction.begin();
      entityManager.merge(entity);
      transaction.commit();
      return entity;
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException("Error updating entitiy:" + entityClass.getSimpleName(), e);
    }
  }

  public void delete(T entity) {
    EntityTransaction transaction = entityManager.getTransaction();

    try {
      transaction.begin();
      entityManager.remove(entity);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException("Error deleting entitiy:" + entityClass.getSimpleName(), e);
    }
  }

  public void deleteById(ID id) {
    findById(id).ifPresentOrElse(this::delete, () -> {
      throw new RuntimeException("Error deleting entitiy:" + entityClass.getSimpleName() + " with id:" + id);
    });
  }
}
