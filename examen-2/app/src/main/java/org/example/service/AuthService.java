package org.example.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.example.model.User;
import org.example.repository.UserRepository;

public class AuthService {
  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public Optional<User> authenticate(String username, String password) {
    return userRepository
        .findByUsername(username)
        .filter(user -> user.getPassword().equals(password));
  }

  @Transactional
  public User register(User user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new IllegalArgumentException("User already exists");
    }
    return userRepository.save(user);
  }
}
