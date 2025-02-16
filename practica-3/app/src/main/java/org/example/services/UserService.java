package org.example.services;

import org.example.exceptions.NotFoundException;
import org.example.models.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.example.util.AccessStatus;
import org.example.util.Role;

public class UserService {
    private static final Map<String, User> users = new HashMap<>();

    public UserService() {

    }

    static {
        User administrator = new User("Theprimeagen", "Ignacio", "1234",Role.ADMIN, AccessStatus.UNAUTHENTICATED);
        users.put(administrator.getUsername(), administrator);
    }

    public Collection<User> getAllUsers() {
        return users.values();
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

    /*public void createUser(User user) {
        users.put(user.getUsername(), user);
    }*/

    public void createUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }
        users.put(user.getUsername(), user);
        System.out.println("Usuario registrado: " + user.getUsername()); // Debugging
    }


    public void updateUser(User user){
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        } else if (!users.containsKey(user.getUsername())) {
            throw new NotFoundException("Username not found");
        } else {
            User myUser = users.get(user.getUsername());
            myUser.setName(user.getName());
            myUser.setAccessStatus(user.isAccessStatus());
            myUser.setPassword(user.getPassword());
            users.put(myUser.getUsername(), myUser);
        }
        
    }

    public void deleteUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        } else if (!users.containsKey(username)) {
            throw new NotFoundException("Username not found");
        } else {
            users.remove(username);
        }

    }

}
