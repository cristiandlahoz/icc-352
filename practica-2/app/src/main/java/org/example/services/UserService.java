package org.example.services;

import org.example.models.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Map<String, User> users = new HashMap<>();

    public UserService() {

    }

    static { }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public void createUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void deleteUserByUsername(String username) {

    }


    public User getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("The argument 'username' cannot be null.");
        }
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("User not found for the provided username.");
        }
        return users.get(username);
    }



}
