package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.User;
import org.example.services.UserService;

import java.util.Collection;

public class UserController {
    private static final UserService userService = new UserService();

    public static void getAllUsers(Context ctx) {
        Collection<User> myUsers = userService.getAllUsers();
        ctx.json(myUsers);

    }

    public static void getUserByUsername(Context ctx) {
        String username = ctx.pathParam("username");
        User myUser = userService.getUserByUsername(username);
        ctx.json(myUser);
    }

    public static void createUser(Context ctx) {
        User myUser = ctx.bodyAsClass(User.class);
        userService.createUser(myUser);
        ctx.status(201);

    }

    public static void updateUser(Context ctx) {
        User myUser = ctx.bodyAsClass(User.class);
        myUser.setUsername(ctx.pathParam("username"));
        userService.updateUser(myUser);
        ctx.status(200);
    }

    public static void deleteUser(Context ctx) {
        String username = ctx.pathParam("usernme");
        userService.deleteUserByUsername(username);
        ctx.status(200);
    }
}
