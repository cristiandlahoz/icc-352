package org.example.controllers;

import io.javalin.http.Context;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;
import org.example.util.enums.Role;
import org.example.util.*;
import org.example.util.enums.SessionKeys;

@Controller(path = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Get(path = "/login")
    public void renderLoginPage(Context ctx) {
        ctx.render("/auth/login.html");
    }

    @Get(path = "/signup")
    public void renderSignupPage(Context ctx) {
        ctx.render("/auth/signup.html");
    }

    @Post(path = "/login")
    public void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        authService.authenticate(username, password)
                .ifPresentOrElse(
                        user -> {
                            ctx.sessionAttribute(SessionKeys.USER.getKey(), user);
                            ctx.redirect(Routes.HOME.getPath());
                        },
                        () -> ctx.redirect(Routes.LOGIN.getPath())
                );
    }

    @Post(path = "/logout")
    public void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect(Routes.HOME.getPath());
    }

    @Post(path = "/signup")
    public void signup(Context ctx) {
        String username = ctx.formParam("username");
        String name = ctx.formParam("name");
        String password = ctx.formParam("password");

        Role role = Role.USER;

        User newUser = new User(username, password, name, role);

        try {
            authService.register(newUser);
            ctx.sessionAttribute(SessionKeys.USER.getKey(), newUser);
            ctx.redirect(Routes.HOME.getPath());
        } catch (IllegalArgumentException e) {
            ctx.redirect(Routes.SIGNUP.getPath());
        }
    }
}
