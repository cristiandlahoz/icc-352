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
        app.post("/login", this::login);
        app.post("/logout", this::logout);
        app.post("/signup", this::signup);
    }

    private void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null) {
            ctx.redirect("/templates/logIn.html?error=missing_fields");
            return;
        }

        try {
            User user = userService.getUserByUsername(username);

            if (!user.getPassword().equals(password)) {
                ctx.redirect("/templates/logIn.html?error=invalid_credentials");
                return;
            }

            ctx.sessionAttribute("USUARIO", user);
            System.out.println("Usuario autenticado: " + user);
            ctx.redirect("/");

        } catch (IllegalArgumentException e) {
            ctx.redirect("/templates/logIn.html?error=user_not_found");
        }
    }

    private void logout(Context ctx) {
        //User user = (User) ctx.req().getSession().getAttribute("currentUser");
        ctx.req().getSession().invalidate();
        //user.setAccessStatus(AccessStatus.UNAUTHENTICATED);
        ctx.redirect("/");
    }

    private void signup(Context ctx) {
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;

        if (name == null || username == null || password == null) {
            ctx.redirect("/templates/signUp.html?error=missing_fields");
            return;
        }

        try {
            userService.getUserByUsername(username);
            ctx.redirect("/templates/signUp.html?error=user_exists");
            return;
        } catch (IllegalArgumentException e) {
            // Usuario no encontrado, se puede crear
        }

        if (isAuthor == true) {
            User newUser = new User(username, name, password, Role.AUTHOR, AccessStatus.UNAUTHENTICATED);
            userService.createUser(newUser);

        } else {
            User newUser = new User(username, name, password, Role.USER,  AccessStatus.UNAUTHENTICATED);
            userService.createUser(newUser);
        }


        User createdUser = userService.getUserByUsername(username);
        if (createdUser != null) {
            createdUser.setAccessStatus(AccessStatus.AUTHENTICATED);
            ctx.sessionAttribute("USUARIO", createdUser);
            System.out.println("Usuario registrado: " + username);
            System.out.println("Usuario autenticado tras registro: " + ctx.sessionAttribute("USUARIO"));
            ctx.redirect("/");
        } else {
            ctx.redirect("/templates/signUp.html?error=registration_failed");
        }
    }
}
