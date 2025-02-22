package org.example.services;

import org.example.exceptions.NotFoundException;
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

    /*public void createUser(User user) {
        users.put(user.getUsername(), user);
    }*/

    public User createUser(User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(user);
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
