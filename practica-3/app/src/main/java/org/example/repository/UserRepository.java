package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.models.User;
import org.example.util.BaseRepository;

public class UserRepository extends BaseRepository<User, String> {
	public UserRepository(EntityManager entityManager) {
		super(entityManager, User.class);
	}
}
