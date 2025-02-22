package org.example.services;

import org.example.exceptions.NotFoundException;
import org.example.models.Article;
import org.example.models.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.example.repository.ArticleRepository;
import org.example.repository.UserRepository;
import org.example.util.AccessStatus;
import org.example.util.Role;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*static {
        User administrator = new User("Theprimeagen", "Ignacio", "1234",Role.ADMIN, AccessStatus.UNAUTHENTICATED);
        users.put(administrator.getUsername(), administrator);
    }*/

    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return userRepository.findById(username);
    }


    public User createUser(String username, String name, String password, Role role, AccessStatus accessStatus) {
        if (username == null || name == null || password == null || role == null || accessStatus == null) {
            throw new IllegalArgumentException("Username and name cannot be null");
        }
        User newUser = new User(username, name, password, role, accessStatus);
        return userRepository.save(newUser);
    }

    public User updateUser(User user) {
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("User and username cannot be null");
        }
        Optional<User> existingUser = userRepository.findById(user.getUsername());
        if (existingUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return userRepository.update(user);
    }

    public void deleteUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        userRepository.deleteById(username);
    }

}
