package org.example.controller;

import io.javalin.http.Context;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Post;
import org.example.util.enums.Role;
import org.example.util.enums.Routes;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller(path = "/users")
public class UserController {
    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Post(path = "/{username}")
    public void updateUser(Context ctx) {
        ctx.attribute(
                "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));
        try {
            String username = ctx.pathParam("username");
            Optional<User> userOptional = userService.getUserByUsername(username);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                String role = existingUser.getRole().toString();
                Map<String, Object> model = new HashMap<>();
                model.put("user", existingUser);
                model.put("role", role);
                ctx.render("/pages/update_user.html", model);
            } else {
                ctx.status(400).result("Error Updating User");
            }
        } catch (Exception e) {
            ctx.status(500).result("Error Updating User: " + e.getMessage());
        }
    }

    @Post(path = "/form/{id}")
    public void formHandler(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("id"));

        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            ctx.status(404).result("User not found");
            return;
        }

        User user = userOptional.get();
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        //boolean isAuthor = ctx.formParam("is_author") != null;

        try {
            userService.updateUser(username, password, name);
            ctx.status(200).redirect(Routes.HOME.getPath());
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
}
