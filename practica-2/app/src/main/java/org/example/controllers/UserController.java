package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.example.models.User;

import java.util.HashMap;
import java.util.Map;


public class UserController {

    private static final Map<String, User> users = new HashMap<>();

    public static void getAllUsers(Context ctx) {
        ctx.json(users);
    }

    public static final Handler createUser = ctx -> {
        User usuario = ctx.bodyAsClass(User.class);

        if (users.containsKey(usuario.getUsername())) {
            ctx.status(400).result("El usuario con username " + usuario.getUsername() + " ya existe.");
        } else {
            users.put(usuario.getUsername(), usuario);
            ctx.status(201).result("Usuario creado exitosamente.");
        }
    };


    public static Handler deleteUser = ctx -> {
        String username = ctx.pathParam("username");

        if (users.containsKey(username)) {
            users.remove(username);
            ctx.status(200).result("Usuario con username " + username + " eliminado correctamente.");
        } else {
            ctx.status(404).result("Usuario con username " + username + " no encontrado.");
        }
    };
}
