package org.example.repository;

import org.example.model.User;
import org.example.util.baseclasses.BaseRepository;

import jakarta.persistence.EntityManager;

public class UserRepository extends BaseRepository<User, Long> {
  public UserRepository(EntityManager entityManager) {
    super(entityManager, User.class);
  }
}
