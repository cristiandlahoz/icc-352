package org.example.services;

import java.util.Optional;
import org.example.models.User;
import org.example.repository.AuthRepository;
import org.example.repository.UserRepository;

public class AuthService {
  private final UserRepository userRepository;
  private final AuthRepository authRepository;

  public AuthService(UserRepository userRepository, AuthRepository authRepository) {
    this.userRepository = userRepository;
    this.authRepository = authRepository;
  }

  public Optional<User> authenticate(String username, String password) {
    return userRepository
        .findByUsername(username)
        .filter(user -> user.getPassword().equals(password))
        .map(
            user -> {
              authRepository.logAuthentication(username);
              return user;
            });
  }

  public User register(User user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new IllegalArgumentException("User already exists");
    }
    return userRepository.save(user);
  }
}
