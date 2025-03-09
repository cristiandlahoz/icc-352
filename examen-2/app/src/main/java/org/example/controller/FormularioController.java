package org.example.controller;

import io.javalin.http.Context;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;

import java.util.List;
import java.util.Map;

@Controller(path = "/forms")
public class FormularioController {

  @Get(path = "/")
  public void index(Context ctx) {
    ctx.result("GET /forms");
  }

  @Get(path = "/update/{id}")
  public void updateForm(Context ctx) {
    ctx.result("GET /forms/update" + ctx.pathParam("id"));
  }

  @Post(path = "/update")
  public void update(Context ctx) {
    ctx.result("POST /forms/update");
  }
}
