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
        app.get(Routes.CREATEUSER.getPath(), this::renderCreateUserPage);
        app.get(Routes.USERS.getPath(), UserController::getAllUsers);
        app.get(Routes.USER.getPath(), UserController::getUserByUsername);
        app.post(Routes.USERS.getPath(), UserController::createUser);
        app.put(Routes.USER.getPath(), UserController::updateUser);
        app.delete(Routes.USER.getPath(), UserController::deleteUser);
    }

    private void renderCreateUserPage(Context ctx) {
        ctx.render("/pages/create_user.html");
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
        User newUser = AuthenticationController.processUserForm(ctx, Routes.CREATEUSER.getPath());
        if (newUser != null) {
            ctx.redirect("/");
        }
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
