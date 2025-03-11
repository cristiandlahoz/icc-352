package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {
  @Mock private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository);
  }

  @Test
  public void testCreateUser_Success() {
    String username = "test";
    String password = "test";
    String name = "test";
    Role role = Role.USER;

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(new User(username, password, name, role));

    Optional<User> createdUser = userService.createUser(username, password, name, role);

    assertTrue(createdUser.isPresent());
    assertEquals(username, createdUser.get().getUsername());
    assertEquals(password, createdUser.get().getPassword());
    assertEquals(name, createdUser.get().getName());
    assertEquals(role, createdUser.get().getRole());
  }

  @Test
  public void testCreateUser_UsernameExists() {
    String username = "test";
    String password = "test";
    String name = "test";
    Role role = Role.USER;

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

    Optional<User> createdUser = userService.createUser(username, password, name, role);

    assertTrue(createdUser.isEmpty());
  }

  @Test
  public void testUpdateUser_success() {
    String username = "testUser";
    String newPassword = "newPassword";
    String newName = "Updated User";

    User existingUser = new User(username, "password123", "Old User", Role.USER);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
    when(userRepository.update(any(User.class))).thenReturn(existingUser);

    Optional<User> updatedUser = userService.updateUser(username, newPassword, newName);

    assertTrue(updatedUser.isPresent());
    assertEquals(newName, updatedUser.get().getName());
    assertEquals(newPassword, updatedUser.get().getPassword());
  }

  @Test
  public void testUpdateUser_userNotFound() {
    String username = "nonExistentUser";
    String password = "password123";
    String name = "Test User";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.updateUser(username, password, name);
        });
  }

  @Test
  public void testDeleteUser_success() {
    String username = "testUser";
    User user = new User(username, "password123", "Test User", Role.USER);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    userService.deleteUser(username);

    verify(userRepository).deleteByUsername(username);
  }

  @Test
  public void testDeleteUser_admin() {
    String username = "adminUser";
    User user = new User(username, "password123", "Admin User", Role.ADMIN);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.deleteUser(username);
        });
  }
}
