package org.example.controller;

import io.javalin.http.Context;
import org.example.model.Encuestado;
import org.example.service.EncuestadoService;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;
import org.example.util.enums.NivelEscolar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(path = "/encuestados")
public class EncuestadoController {

  private EncuestadoService encuestadoService;

  public EncuestadoController(EncuestadoService encuestadoService) {
    this.encuestadoService = encuestadoService;
  }

  @Get(path = "/")
  public void getEncuestados(Context ctx) {
    List<Encuestado> encuestados = encuestadoService.findAll();
    Map<String, Object> model = new HashMap<>();
    model.put("titulo", "Encuestado list");
    model.put("lista", encuestados);
    ctx.render("listar.html", model);
  }

  @Get(path = "/create")
  public void createEncuestadoForm(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("titulo", "Create Encuestado");
    model.put("accion", "/create");
    ctx.render("crearEditarVisualizar.html", model);
  }

  @Post(path = "/create")
  public void createEncuestado(Context ctx) {
    String nombre = ctx.formParam("nombre");
    String sector = ctx.formParam("sector");
    String nivelEscolarParam = ctx.formParam("nivelEscolar");

    try {
      NivelEscolar nivelEscolar = NivelEscolar.valueOf(nivelEscolarParam);
      encuestadoService.createEncuestado(nombre, sector, nivelEscolar);
    } catch (IllegalArgumentException e) {
      System.out.println("Error: Nivel escolar inválido.");
      ctx.status(400).result("Nivel escolar inválido.");
      return;
    } catch (Exception e) {
      System.out.println("Error creating encuestado: " + e.getMessage());
      ctx.status(500).result("Error al crear el encuestado.");
      return;
    }

    ctx.redirect("/encuestados");
  }

  @Get(path = "/update/{id}")
  public void editEncuestadoForm(Context ctx) {
    Long encuestadoId;
    try {
      encuestadoId = Long.parseLong(ctx.pathParam("encuestadoId"));
    } catch (NumberFormatException e) {
      ctx.status(400).result("ID de encuestado inválido.");
      return;
    }

    encuestadoService.findById(encuestadoId).ifPresentOrElse(
        encuestado -> {
          Map<String, Object> model = new HashMap<>();
          model.put("titulo", "Editar encuestado " + encuestado.getEncuestadoId());
          model.put("encuestado", encuestado);
          model.put("accion", "/editar");
          ctx.render("crearEditarVisualizar.html", model);
        },
        () -> ctx.status(404).result("Encuestado no encontrado"));
  }

  @Post(path = "/update")
  public void updateEncuestado(Context ctx) {
    Long encuestadoId = Long.parseLong(ctx.formParam("encuestadoId"));
    String nombre = ctx.formParam("nombre");
    String sector = ctx.formParam("sector");
    String nivelEscolarParam = ctx.formParam("nivelEscolar");

    try {
      NivelEscolar nivelEscolar = NivelEscolar.valueOf(nivelEscolarParam);
      encuestadoService.updateEncuestado(encuestadoId, nombre, sector, nivelEscolar);
    } catch (IllegalArgumentException e) {
      System.out.println("Error: Nivel escolar inválido.");
      ctx.status(400).result("Nivel escolar inválido.");
      return;
    } catch (Exception e) {
      System.out.println("Error updating encuestado: " + e.getMessage());
      ctx.status(500).result("Error al actualizar el encuestado.");
      return;
    }

    ctx.redirect("/encuestados");
  }

  @Post(path = "/delete/{id}")
  public void deleteEncuestado(Context ctx) {
    Long encuestadoId;
    try {
      encuestadoId = Long.parseLong(ctx.pathParam("id"));
      encuestadoService.deleteEncuestado(encuestadoId);
    } catch (NumberFormatException e) {
      System.out.println("Error: ID inválido.");
      ctx.status(400).result("ID inválido.");
      return;
    } catch (Exception e) {
      System.out.println("Error deleting encuestado: " + e.getMessage());
      ctx.status(500).result("Error al eliminar el encuestado.");
      return;
    }

    ctx.redirect("/encuestados");
  }
}
