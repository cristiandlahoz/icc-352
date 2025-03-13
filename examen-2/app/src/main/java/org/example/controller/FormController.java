package org.example.controller;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

import org.example.dto.FormDTO;
import org.example.model.User;
import org.example.service.EncuestadoService;
import org.example.service.FormService;
import org.example.service.LocationService;
import org.example.service.UserService;
import org.example.util.annotations.*;
import org.example.util.enums.NivelEscolar;
import org.example.util.enums.SessionKeys;

@Controller(path = "/forms")
public class FormController {

  private FormService formService;
  private UserService userService;
  private EncuestadoService encuestadoService;
  private LocationService locationService;

  public FormController(
      FormService formService,
      UserService userService,
      EncuestadoService encuestadoService,
      LocationService locationService) {
    this.formService = formService;
    this.userService = userService;
    this.encuestadoService = encuestadoService;
    this.locationService = locationService;
  }

  @Get(path = "/")
  public void getForm(Context ctx) {
    ctx.result("This is not working by the moment");
  }

  @Get(path = "/create")
  public void getCreateForm(Context ctx) {
    if (ctx.sessionAttribute(SessionKeys.USER.getKey()) == null) {
      ctx.status(401).redirect("/auth/login");
      return;
    }
    User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    Map<String, Object> model = new HashMap<>() {
      {
        put("username", user.getUsername());
      }
    };
    ctx.render("pages/form.html", model);
  }

    @Post(path = "/create")
    public void postCreateForm(Context ctx) {
        try {
            FormDTO surveyData = ctx.bodyAsClass(FormDTO.class);

            if (surveyData == null || surveyData.username() == null) {
                ctx.status(400).result("❌ Error: Datos incompletos o nulos.");
                return;
            }

            // Proceso normal
            userService.getUserByUsername(surveyData.username())
                    .ifPresentOrElse(
                            u -> {
                                locationService.createLocation(surveyData.latitude(), surveyData.longitude())
                                        .ifPresentOrElse(
                                                l -> {
                                                    encuestadoService.createEncuestado(
                                                            surveyData.fullname(),
                                                            surveyData.sector(),
                                                            NivelEscolar.valueOf(surveyData.education().replace(" ", "_").toUpperCase())
                                                    ).ifPresentOrElse(
                                                            e -> {
                                                                formService.createForm(u, l, e, surveyData.isSynchronized());
                                                                ctx.status(200).result("✅ Encuesta guardada correctamente.");
                                                            },
                                                            () -> ctx.status(400).result("❌ Error creando encuestado.")
                                                    );
                                                },
                                                () -> ctx.status(400).result("❌ Error creando ubicación.")
                                        );
                            },
                            () -> ctx.status(400).result("❌ Usuario no encontrado.")
                    );
        } catch (Exception e) {
            ctx.status(500).result("❌ Error interno del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Get(path = "/update/{id}")
  public void getUpdateForm(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));
    formService
        .getFormById(id)
        .ifPresentOrElse(
            f -> {
              ctx.render("pages/form.html");
            },
            () -> {
              ctx.status(404).result("Form not found");
            });
  }

  @Post(path = "/update/{id}")
  public void postUpdateForm(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));
    String username = ctx.formParam("username");
    Double latitude = Double.valueOf(ctx.formParam("latitude"));
    Double longitude = Double.valueOf(ctx.formParam("longitude"));
    Boolean isShynchronized = Boolean.parseBoolean(ctx.formParam("isSynchronized"));

    userService
        .getUserByUsername(username)
        .ifPresentOrElse(
            u -> {
              locationService
                  .getLocationByCoordinates(latitude, longitude)
                  .ifPresentOrElse(
                      l -> {
                        formService.updateForm(id, isShynchronized);
                        ctx.status(200).redirect("/forms");
                      },
                      () -> {
                        ctx.status(400).result("Location not found");
                      });
            },
            () -> {
              ctx.status(400).result("User not found");
            });
  }

  @Post(path = "/delete/{id}")
  public void deleteForm(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));
    formService.deleteForm(id);
    ctx.status(200).redirect("/forms");
  }
}
