package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.example.models.User;
import org.example.services.UserService;
import org.example.util.AccessStatus;
import org.example.util.BaseController;
import org.example.util.Role;

public class AuthenticationController extends BaseController {

    private static final UserService userService = new UserService();

    public AuthenticationController(Javalin app) {
        super(app);
    }

    public void applyRoutes() {
        app.get("/login", ctx -> {
            ctx.render("/public/templates/auth/logIn.html");
        });
        app.post("/login", AuthenticationController::login);
        app.post("/logout", AuthenticationController::logout);
        app.post("/signup", AuthenticationController::signup);
    }

    private static void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null) {
            ctx.render("/templates/auth/logIn.html");
            return;
        }

        try {
            User user = userService.getUserByUsername(username);

            if (!user.getPassword().equals(password)) {
                ctx.redirect("/templates/auth/logIn.html?error=invalid_credentials");
                return;
            }

            ctx.sessionAttribute("USUARIO", user);
            System.out.println("Usuario autenticado: " + user);
            ctx.redirect("/");

        } catch (IllegalArgumentException e) {
            ctx.redirect("/templates/auth/logIn.html?error=user_not_found");
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void signup(Context ctx) {
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;

        if (name == null || username == null || password == null) {
            ctx.redirect("/templates/auth/signUp.html?error=missing_fields");
            return;
        }

        try {
            userService.getUserByUsername(username);
            ctx.redirect("/templates/auth/signUp.html?error=user_exists");
            return;
        } catch (IllegalArgumentException e) {
            // Usuario no encontrado, se puede crear
        }

        Role role = isAuthor ? Role.AUTHOR : Role.USER;
        User newUser = new User(username, name, password, role, AccessStatus.UNAUTHENTICATED);
        userService.createUser(newUser);

        User createdUser = userService.getUserByUsername(username);
        if (createdUser != null) {
            createdUser.setAccessStatus(AccessStatus.AUTHENTICATED);
            ctx.sessionAttribute("USUARIO", createdUser);
            System.out.println("Usuario registrado: " + username);
            System.out.println("Usuario autenticado tras registro: " + ctx.sessionAttribute("USUARIO"));
            ctx.redirect("/");
        } else {
            ctx.redirect("/templates/auth/signUp.html?error=registration_failed");
        }
    }
}
