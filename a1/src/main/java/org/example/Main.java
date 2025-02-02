package org.example;

import  io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress = false;
                staticFileConfig.aliasCheck = null;
            });}).get("/", ctx -> ctx.result("Hello World")).start(7070);


        app.before("*", ctxContext -> {
            if(ctxContext.cookie("user") == null || ctxContext.cookie("password")== null){//no ha realizado el proceso de login.
                ctxContext.redirect("/formulario.html");
                return;
            }
            ctxContext.result("Hola "+ctxContext.cookie("nombre"));
        });

        app.post("/login-cookies", ctxContext -> {
            //recibiendo información del formulario.
            String usuario = ctxContext.formParam("user");
            String contrasena = ctxContext.formParam("password");
            if(usuario==null || contrasena == null){
                //errror para procesar la información.
                ctxContext.redirect("/formulario.html");
                return;
            }
            //Estamos haciendo fake de un servicio de autenticacion, busque en un servicio.
            ctxContext.cookie("user", usuario, 120);
            ctxContext.cookie("password", contrasena, 120);
            //enviando a la vista.
            ctxContext.redirect("/");
        });
    }
}