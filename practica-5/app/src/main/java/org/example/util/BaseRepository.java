package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class BaseRepository<T, ID> {
  protected final EntityManager entityManager;
  protected final Class<T> entityClass;

  public BaseRepository(EntityManager entityManager, Class<T> entityClass) {
    this.entityManager = entityManager;
    this.entityClass = entityClass;
  }

  public Optional<T> findById(ID id) {
    return Optional.ofNullable(entityManager.find(entityClass, id));
  }

  public List<T> findAll() {
    return entityManager.createQuery("FROM " + entityClass.getName()).getResultList();
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
      throw new RuntimeException("Error saving entity: " + entityClass.getName(), e);
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
      throw new RuntimeException(e);
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
      throw new RuntimeException(e);
    }
  }

  public void deleteById(ID id) {
    findById(id).ifPresent(this::delete);
  }
}
