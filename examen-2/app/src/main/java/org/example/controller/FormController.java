package org.example.controller;

import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.example.service.EncuestadoService;
import org.example.service.FormService;
import org.example.service.LocationService;
import org.example.service.UserService;
import org.example.util.annotations.*;
import org.example.util.enums.NivelEscolar;

@Controller(path = "/forms")
@NoArgsConstructor
@AllArgsConstructor
public class FormController {

  private FormService formService;
  private UserService userService;
  private EncuestadoService encuestadoService;
  private LocationService locationService;

  @Get(path = "/")
  public void getForm(Context ctx) {
    ctx.result("This is not working by the moment");
  }

  @Get(path = "/create")
  public void getCreateForm(Context ctx) {
    ctx.render("pages/form.html");
  }

  @Post(path = "/create")
  public void postCreateForm(Context ctx) {
    String username = ctx.formParam("username");
    String fullname = ctx.formParam("fullname");
    String sector = ctx.formParam("sector");
    NivelEscolar nivelEscolar = NivelEscolar.valueOf(ctx.formParam("education"));
    Double latitude = Double.valueOf(ctx.formParam("latitude"));
    Double longitude = Double.valueOf(ctx.formParam("longitude"));
    Boolean isShynchronized = Boolean.parseBoolean(ctx.formParam("isSynchronized"));

    userService.getUserByUsername(username).ifPresentOrElse(u -> {
      locationService.createLocation(latitude, longitude).ifPresentOrElse(l -> {
        encuestadoService.createEncuestado(fullname, sector, nivelEscolar).ifPresentOrElse(e -> {
          formService.createForm(u, l, e, isShynchronized);
          ctx.status(200).redirect("/forms");
        }, () -> {
          ctx.status(400).result("Error creating encuestado");
        });
      }, () -> {
        ctx.status(400).result("Error creating location");
      });
    }, () -> {
      ctx.status(400).result("User not found");
    });
  }

  @Get(path = "/update/{id}")
  public void getUpdateForm(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));
    formService.getFormById(id).ifPresentOrElse(f -> {
      ctx.render("pages/form.html");
    }, () -> {
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

    userService.getUserByUsername(username).ifPresentOrElse(u -> {
      locationService.getLocationByCoordinates(latitude, longitude).ifPresentOrElse(l -> {
        formService.updateForm(id, isShynchronized);
        ctx.status(200).redirect("/forms");
      }, () -> {
        ctx.status(400).result("Location not found");
      });
    }, () -> {
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
