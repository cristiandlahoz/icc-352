package org.example.repository;

import org.example.util.baseclasses.BaseRepository;
import org.h2.engine.User;

import jakarta.persistence.EntityManager;

public class UserRepository extends BaseRepository<User, Long> {
  public UserRepository(EntityManager entityManager) {
    super(entityManager, User.class);
  }
}
