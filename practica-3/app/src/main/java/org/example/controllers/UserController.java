package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.*;

import org.example.models.Photo;
import org.example.models.User;
import org.example.services.UserService;
import org.example.util.*;
import org.hibernate.Hibernate;

public class UserController extends BaseController {
    private final UserService userService;

    public UserController(Javalin app, UserService userService) {
        super(app);
        this.userService = userService;
    }

    public void applyRoutes() {
        app.get(Routes.CREATEUSER.getPath(), this::renderCreateUserPage);
        app.get(Routes.MANAGEUSERS.getPath(), this::renderManageUsers);
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

    private void renderManageUsers(Context ctx) {
        ctx.render("/pages/manage_users.html");
    }

    public void getAllUsers(Context ctx) {
        Collection<User> myUsers = userService.getAllUsers();
        myUsers.forEach(
                user -> {
                    if (user.getProfilePhoto() != null) {
                        Hibernate.initialize(user.getProfilePhoto()); // Fuerza la carga de profilePhoto
                    }
                });
        ctx.json(myUsers);
    }

    public void getUserByUsername(Context ctx) {
        String username = ctx.pathParam("username");
        User myUser = userService.getUserByUsername(username).orElse(null);
        ctx.json(myUser);
    }

  /*public void createUser(Context ctx) {
    ctx.attribute(
        "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));
    User newUser = processUserForm(ctx, Routes.USERS.getPath());
    if (newUser != null) {
      ctx.redirect("/");
    } else {
      ctx.status(400).result("Error Creating User HOLA");
    }
  }*/

    public void createUser(Context ctx) {
        System.out.println("➡️ Iniciando creación de usuario...");

        // Configurar multipart para permitir la subida de archivos
        ctx.attribute(
                "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));

        System.out.println("✔️ Multipart configurado correctamente.");

        // Procesar formulario
        User newUser = processUserForm(ctx, Routes.USERS.getPath());

        if (newUser != null) {
            System.out.println("✅ Usuario creado con éxito: " + newUser.getUsername());
            ctx.redirect("/");
        } else {
            System.out.println("❌ Error al crear usuario. Verificando posibles causas...");

            // Verificar si el usuario ya existe
            String username = ctx.formParam("username");
            Optional<User> existingUser = userService.getUserByUsername(username);
            if (existingUser.isPresent()) {
                System.out.println("⚠️ El nombre de usuario ya existe: " + username);
                ctx.status(400).result("Error: Username already exists.");
                return;
            }

            // Verificar si algún campo está vacío
            String name = ctx.formParam("name");
            String password = ctx.formParam("password");
            if (name == null || username == null || password == null) {
                System.out.println("⚠️ Campos faltantes: name=" + name + ", username=" + username + ", password=" + password);
                ctx.status(400).result("Error: Missing required fields.");
                return;
            }

            // Verificar si el proceso de creación falló por otra razón
            System.out.println("⚠️ Error desconocido al crear el usuario.");
            ctx.status(400).result("Error Creating User.");
        }
    }


    public void updateUser(Context ctx) {
        ctx.attribute(
                "org.eclipse.jetty.multipartConfig", new jakarta.servlet.MultipartConfigElement("/tmp"));
        try {
            String username = ctx.pathParam("username");
            userService
                    .getUserByUsername(username)
                    .ifPresentOrElse(
                            existingUser -> {
                                String role = existingUser.getRole().toString();
                                Map<String, Object> model = setModel("user", existingUser, "role", role);
                                ctx.render("/pages/update_user.html", model);
                            },
                            () -> ctx.status(400).result("Error Updating User"));
        } catch (Exception e) {
            ctx.status(500).result("Error Updating User" + e.getMessage());
        }
    }

    public void formHandler(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("id"));

        // Obtener el usuario antes de modificar
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            ctx.status(404).result("User not found");
            return;
        }

        Photo oldProfilePhoto = user.getProfilePhoto();


        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;
        Role role = isAuthor ? Role.AUTHOR : Role.USER;


        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);

        // Procesar la nueva foto si se ha subido
        if (ctx.uploadedFile("profilePhoto") != null) {
            try {
                var uploadedFile = ctx.uploadedFile("profilePhoto");
                byte[] bytes = uploadedFile.content().readAllBytes();
                String encodedString = Base64.getEncoder().encodeToString(bytes);
                Photo newPhoto =
                        new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
                user.setProfilePhoto(newPhoto);
                uploadedFile.content().close();
            } catch (IOException e) {
                e.printStackTrace();
                ctx.status(500).result("Error processing profile photo.");
                return;
            }
        }

        if (user.getProfilePhoto().getFotoBase64().isEmpty()) {
            user.setProfilePhoto(oldProfilePhoto);

        }

        userService.updateUser(user);
        ctx.status(200).redirect(Routes.HOME.getPath());
        return;
    }

    public void deleteUser(Context ctx) {
        String username = ctx.pathParam("username");
        userService.deleteUserByUsername(username);
        ctx.status(200);
    }

  /*protected User processUserForm(Context ctx, String redirectRoute) {
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

    // Procesar la foto de perfil si se ha subido
    Photo profilePhoto = null;
    if (ctx.uploadedFile("profilePhoto") != null) {
      try {
        var uploadedFile = ctx.uploadedFile("profilePhoto");
        byte[] bytes = uploadedFile.content().readAllBytes();
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        profilePhoto =
            new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
      } catch (IOException e) {
        e.printStackTrace();
        ctx.status(500).result("Error processing profile photo.");
        return null;
      }
    }

    return userService.createUser(
        username, name, password, role, AccessStatus.UNAUTHENTICATED, profilePhoto);
  }
*/

    protected User processUserForm(Context ctx, String redirectRoute) {
        System.out.println("➡️ Iniciando procesamiento del formulario...");

        // Obtener parámetros del formulario
        String name = ctx.formParam("name");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean isAuthor = ctx.formParam("is_author") != null;

        System.out.println("📌 Datos recibidos -> name: " + name + ", username: " + username + ", password: " + (password != null ? "****" : "null") + ", isAuthor: " + isAuthor);

        // Verificar si algún campo obligatorio está vacío
        if (name == null || username == null || password == null) {
            System.out.println("❌ Error: Campos faltantes en el formulario.");
            ctx.redirect(redirectRoute);
            return null;
        }

        // Verificar si el usuario ya existe
        try {
            Optional<User> existingUser = userService.getUserByUsername(username);
            if (existingUser.isPresent()) {
                System.out.println("⚠️ Usuario ya existe en la base de datos: " + username);
                ctx.redirect(redirectRoute);
                return null;
            }
        } catch (Exception e) {
            System.out.println("✅ Usuario no encontrado, se puede crear. Exception: " + e.getMessage());
        }

        // Asignar rol
        Role role = isAuthor ? Role.AUTHOR : Role.USER;
        System.out.println("✔️ Rol asignado: " + role);

        // Procesar la foto de perfil si se subió
        Photo profilePhoto = null;
        if (ctx.uploadedFile("profilePhoto") != null) {
            try {
                var uploadedFile = ctx.uploadedFile("profilePhoto");
                System.out.println("📷 Foto de perfil subida: " + uploadedFile.filename());

                byte[] bytes = uploadedFile.content().readAllBytes();
                String encodedString = Base64.getEncoder().encodeToString(bytes);
                profilePhoto = new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
                System.out.println("✔️ Foto de perfil procesada con éxito.");
            } catch (IOException e) {
                System.out.println("❌ Error al procesar la foto de perfil: " + e.getMessage());
                e.printStackTrace();
                ctx.status(500).result("Error processing profile photo.");
                return null;
            }
        } else {
            System.out.println("📌 No se subió foto de perfil.");
        }

        // Intentar crear el usuario en la base de datos
        try {
            System.out.println("🛠️ Intentando crear usuario en la base de datos...");
            User createdUser = userService.createUser(username, name, password, role, AccessStatus.UNAUTHENTICATED, profilePhoto);
            System.out.println("✅ Usuario creado correctamente: " + createdUser.getUsername());
            return createdUser;
        } catch (Exception e) {
            System.out.println("❌ Error al guardar usuario en la base de datos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
