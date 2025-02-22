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
    private final UserService userService;
    public UserController(Javalin app, UserService userService) {
        super(app);
        this.userService = userService;
    }

    public void applyRoutes() {
        app.get(Routes.CREATEUSER.getPath(), this::renderCreateUserPage);
        app.get(Routes.USERS.getPath(), this::getAllUsers);
        app.get(Routes.USER.getPath(), this::getUserByUsername);
        app.post(Routes.USERS.getPath(), this::createUser);
        app.put(Routes.USER.getPath(), this::updateUser);
        app.delete(Routes.USER.getPath(), this::deleteUser);
    }

    private void renderCreateUserPage(Context ctx) {
        ctx.render("/pages/create_user.html");
    }

    public void getAllUsers(Context ctx) {
        Collection<User> myUsers = userService.getAllUsers();
        ctx.json(myUsers);

    }

    public void getUserByUsername(Context ctx) {
        String username = ctx.pathParam("username");
        User myUser = userService.getUserByUsername(username).orElse(null);
        ctx.json(myUser);
    }


    public void createUser(Context ctx) {
        User newUser = processUserForm(ctx, Routes.CREATEUSER.getPath());
        if (newUser != null) {
            ctx.redirect("/");
        }
    }
    public void updateUser(Context ctx) {
        User myUser = ctx.bodyAsClass(User.class);
        myUser.setUsername(ctx.pathParam("username"));
        userService.updateUser(myUser);
        ctx.status(200);
    }

    public void deleteUser(Context ctx) {
        String username = ctx.pathParam("usernme");
        userService.deleteUserByUsername(username);
        ctx.status(200);
    }

    protected User processUserForm(Context ctx, String redirectRoute) {
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;

        if (name == null || username == null || password == null) {
            ctx.redirect(redirectRoute);
            return null;
        }

        try {
            userService.getUserByUsername(username);
            ctx.redirect(redirectRoute);
            return null;
        } catch (IllegalArgumentException e) {
            // Usuario no encontrado, se puede crear
        }

        Role role = isAuthor ? Role.AUTHOR : Role.USER;

        return userService.createUser(username, name, password, role, AccessStatus.UNAUTHENTICATED);
    }
}
