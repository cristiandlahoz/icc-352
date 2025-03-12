package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import org.example.model.User;
import org.example.util.baseclasses.BaseRepository;

public class UserRepository extends BaseRepository<User, Long> {
  public UserRepository(EntityManager entityManager) {
    super(entityManager, User.class);
  }

  public Optional<User> findByUsername(String username) {
    try {
      User user = entityManager
          .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
          .setParameter("username", username)
          .getSingleResult();
      return Optional.of(user);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public void deleteByUsername(String username) {
    EntityTransaction transaction = entityManager.getTransaction();

    try {
      transaction.begin();
      entityManager
          .createQuery("DELETE FROM User u WHERE u.username = :username")
          .setParameter("username", username)
          .executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException("Error deleting user by username: " + username, e);
    }
  }
}
