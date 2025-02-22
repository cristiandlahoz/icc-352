package org.example.services;

import org.example.models.User;
import org.example.repository.UserRepository;

import java.util.Optional;

public class AuthService {
	private final UserRepository userRepository;

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<User> authenticate(String username, String password) {
		return userRepository.findById(username)
				.filter(user -> user.getPassword().equals(password));
	}

	public User register(User user) {
		if (userRepository.findById(user.getUsername()).isPresent()) {
			throw new IllegalArgumentException("User already exists");
		}
		return userRepository.save(user);
	}
}