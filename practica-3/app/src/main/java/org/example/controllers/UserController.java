package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.models.User;
import org.example.services.UserService;
import org.example.util.AccessStatus;
import org.example.util.BaseController;
import org.example.util.Role;
import org.example.util.Routes;

import java.util.Collection;

public class UserController extends BaseController {
    private static final UserService userService = new UserService();

    public UserController(Javalin app) {
        super(app);

    }

    public void applyRoutes() {
        app.get(Routes.CREATEUSER.getPath(), ctx -> {
            ctx.render("/public/templates/pages/create_user.html");
        });
        app.get("/users", UserController::getAllUsers);
        app.get("/users/{username}", UserController::getUserByUsername);
        app.post("/users/", UserController::createUser);
        app.put("/users/{username}", UserController::updateUser);
        app.delete("/users/{username}", UserController::deleteUser);
    }

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
        /*
         * User myUser = ctx.bodyAsClass(User.class);
         * userService.createUser(myUser);
         * ctx.status(201);
         */
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;

        if (name == null || username == null || password == null) {
            ctx.redirect("/templates/auth/create_user.html?error=missing_fields");
            return;
        }

        try {
            userService.getUserByUsername(username);
            ctx.redirect("/templates/auth/create_user.html?error=user_exists");
            return;
        } catch (IllegalArgumentException e) {
        }

        Role role = isAuthor ? Role.AUTHOR : Role.USER;
        User newUser = new User(username, name, password, role, AccessStatus.UNAUTHENTICATED);
        userService.createUser(newUser);

        ctx.status(201).redirect("/");
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
