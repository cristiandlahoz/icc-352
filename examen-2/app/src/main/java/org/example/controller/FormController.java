package org.example.controller;

import io.javalin.http.Context;
import org.example.util.annotations.*;

@Controller(path = "/forms")
public class FormController {

  @Get(path = "/")
  public void getForm(Context ctx) {
    ctx.html("<form method='post'><input name='input' /><button>Submit</button></form>");
  }
}
