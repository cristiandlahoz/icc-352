package org.example.services;

import java.util.Collection;
import java.util.Optional;
import org.example.exceptions.NotFoundException;
import org.example.models.Photo;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.example.util.AccessStatus;
import org.example.util.Role;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Collection<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserByUsername(String username) {
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null");
    }
    return userRepository.findByUsername(username);
  }

  public User getUserByUserId(Long userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public User createUser(
      String username,
      String name,
      String password,
      Role role,
      AccessStatus accessStatus,
      Photo profilePhoto) {
    if (username == null
        || name == null
        || password == null
        || role == null
        || accessStatus == null) {
      throw new IllegalArgumentException(
          "Username, name, password, role, and accessStatus cannot be null");
    }

    Optional<User> existingUser = userRepository.findByUsername(username);
    if (existingUser.isPresent()) {
      throw new IllegalArgumentException("Username already exists");
    }

    User newUser = new User(username, name, password, role, accessStatus, profilePhoto);
    return userRepository.save(newUser);
  }

  public User updateUser(User user) {
    if (user == null || user.getUsername() == null) {
      throw new IllegalArgumentException("User and username cannot be null");
    }

    Optional<User> existingUser = userRepository.findById(user.getUserId());
    if (existingUser.isEmpty()) {
      throw new NotFoundException("User not found");
    }

    Optional<User> userWithSameUsername = userRepository.findByUsername(user.getUsername());
    if (userWithSameUsername.isPresent()
        && !userWithSameUsername.get().getUserId().equals(user.getUserId())) {
      throw new IllegalArgumentException("Username already exists");
    }

    // Si el usuario no ha subido una nueva foto, conservamos la anterior
    if (user.getProfilePhoto() == null) {
      user.setProfilePhoto(existingUser.get().getProfilePhoto());
    }

    return userRepository.update(user);
  }

  public void deleteUserByUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }

    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new NotFoundException("User not found");
    }

    userRepository.deleteById(userOptional.get().getUserId());
  }
}
