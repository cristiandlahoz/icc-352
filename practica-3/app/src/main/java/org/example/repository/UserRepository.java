package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.models.User;
import org.example.util.BaseRepository;
import java.util.Optional;

public class UserRepository extends BaseRepository<User, Long> {
	public UserRepository(EntityManager entityManager) {
		super(entityManager, User.class);
	}

	public Optional<User> findByUsername(String username) {
		return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", username)
				.getResultStream()
				.findFirst();
	}
}
