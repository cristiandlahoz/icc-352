package org.example.controller;

import io.javalin.http.Context;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;

@Controller(path = "/formularios")
public class FormularioController {

  @Get(path = "/")
  public void index(Context ctx) {
    ctx.result("GET /formulario");
  }

  @Get(path = "/update/{id}")
  public void updateForm(Context ctx) {
    ctx.result("GET /formulario/update" + ctx.pathParam("id"));
  }

  @Post(path = "/update")
  public void update(Context ctx) {
    ctx.result("POST /formulario/update");
  }
}
