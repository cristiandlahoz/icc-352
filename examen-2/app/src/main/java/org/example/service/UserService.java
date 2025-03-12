package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.enums.Role;

public class UserService {
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> createUser(String username, String password, String name, Role role) {
    if (userRepository.findByUsername(username).isPresent()) {
      return Optional.empty();
    } else if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    } else if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    } else if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be empty");
    } else if (role == null) {
      throw new IllegalArgumentException("Role cannot be empty");
    } else {
      return Optional.of(userRepository.save(new User(username, password, name, role)));
    }
  }

  public Optional<User> updateUser(String username, String password, String name) {
    if (!userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("User not found");
    } else if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    } else if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    } else if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be empty");
    } else {
      User user = getUserByUsername(username).get();
      user.setName(name);
      user.setPassword(password);
      user.setUsername(username);
      return Optional.of(userRepository.update(user));
    }
  }

  public void deleteUser(String username) {
    if (getUserByUsername(username).isEmpty())
      throw new IllegalArgumentException("User not found");
    else if (username == null || username.isEmpty())
      throw new IllegalArgumentException("Username cannot be empty");
    else if (getUserByUsername(username).get().getRole() == Role.ADMIN)
      throw new IllegalArgumentException("Cannot delete admin user");
    else
      userRepository.deleteByUsername(username);
  }
}
