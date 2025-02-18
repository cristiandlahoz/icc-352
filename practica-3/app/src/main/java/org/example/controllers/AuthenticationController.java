package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.example.models.User;
import org.example.services.UserService;
import org.example.util.AccessStatus;
import org.example.util.BaseController;
import org.example.util.Role;
import org.example.util.Routes;

public class AuthenticationController extends BaseController {

    private static final UserService userService = new UserService();

    public AuthenticationController(Javalin app) {
        super(app);
    }

    public void applyRoutes() {
        app.get(Routes.LOGIN.getPath(), this::renderLoginPage);
        app.get(Routes.SIGNUP.getPath(), this::renderSignupPage);

        app.post(Routes.LOGIN.getPath(), AuthenticationController::login);
        app.post(Routes.LOGOUT.getPath(), AuthenticationController::logout);
        app.post(Routes.SIGNUP.getPath(), AuthenticationController::signup);
    }

    private void renderLoginPage(Context ctx) {
        ctx.render("/public/templates/auth/logIn.html");
    }

    private void renderSignupPage(Context ctx) {
        ctx.render("/public/templates/auth/signUp.html");
    }

    private static void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null) {
            ctx.redirect(Routes.LOGIN.getPath());
            return;
        }

        try {
            User user = userService.getUserByUsername(username);

            if (!user.getPassword().equals(password)) {
                ctx.redirect(Routes.LOGIN.getPath());
                return;
            }

            ctx.sessionAttribute("USUARIO", user);
            System.out.println("Usuario autenticado: " + user);
            ctx.redirect("/");

        } catch (IllegalArgumentException e) {
            ctx.redirect(Routes.LOGIN.getPath());
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void signup(Context ctx) {
        User newUser = processUserForm(ctx, Routes.SIGNUP.getPath());
        if (newUser != null) {
            newUser.setAccessStatus(AccessStatus.AUTHENTICATED);
            ctx.sessionAttribute("USUARIO", newUser);
            System.out.println("Usuario registrado: " + newUser.getUsername());
            System.out.println("Usuario autenticado tras registro: " + ctx.sessionAttribute("USUARIO"));
            ctx.redirect("/");
        }
    }

    protected static User processUserForm(Context ctx, String redirectRoute) {
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
        User newUser = new User(username, name, password, role, AccessStatus.UNAUTHENTICATED);
        userService.createUser(newUser);

        return userService.getUserByUsername(username);
    }

}
