package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.models.Article;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.UserService;
import org.example.util.*;

import java.util.*;

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
        app.post(Routes.USER.getPath(), this::updateUser);
        app.delete(Routes.USER.getPath(), this::deleteUser);
        app.post("/users/form/{id}", this::formHandler);
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
        }else {
            ctx.status(400).result("Error Creating User");
        }
    }
    public void updateUser(Context ctx) {
        try {
            String username = ctx.pathParam("username");
            User existingUser = userService.getUserByUsername(username).orElse(null);
            if (existingUser == null) {
                ctx.status(400).result("Error Updating User");
                return;
            }
            Map<String, Object> model = setModel("user", existingUser);
            ctx.render("/pages/update_user.html", model);
        }catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error Updating User");
        }
    }

    public  void formHandler(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("id"));
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;
        Role role = isAuthor ? Role.AUTHOR : Role.USER;


        User user = userService.getUserByUserId(userId);
        if (user == null) {
            ctx.status(404).result("User not found");
            return;
        }
        user.setUsername(username);
        user.setName(name);
        user.setRole(Role.USER);
        user.setPassword(password);
        user.setRole(role);

        userService.updateUser(user);
        ctx.status(200).redirect("/users/" + userId);
        return;
    }


    public void deleteUser(Context ctx) {
        String username = ctx.pathParam("username");
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
