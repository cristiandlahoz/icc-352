package org.example.repository;

import java.util.Optional;

import org.example.model.User;
import org.example.util.baseclasses.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserRepository extends BaseRepository<User, Long> {
  public UserRepository(EntityManager entityManager) {
    super(entityManager, User.class);
  }

  public Optional<User> findByUsername(String username) {
    return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
        .setParameter("username", username)
        .getResultList()
        .stream()
        .findFirst();
  }

  public void deleteByUsername(String username) {
    EntityTransaction transaction = entityManager.getTransaction();

    try {
      transaction.begin();
      entityManager.createQuery("DELETE FROM User u WHERE u.username = :username")
          .setParameter("username", username)
          .executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException("Error deleting user by username: " + username, e);
    }
  }
}
