package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.IOException;
import java.util.*;
import org.example.models.Photo;
import org.example.services.PhotoService;
import org.example.util.BaseController;
import org.example.util.Routes;
import org.jetbrains.annotations.NotNull;

public class PhotoController extends BaseController {
  private final PhotoService photoService;

  public PhotoController(Javalin app, PhotoService photoService) {
    super(app);
    this.photoService = photoService;
  }

  @Override
  public void applyRoutes() {
    app.get(Routes.PHOTOLIST.getPath(), this::renderPhotoListar);
  }

  private void renderPhotoListar(Context ctx) {
    ctx.render("/templates/listar.html");
  }

  public void getAllPhotos(Context ctx) {
    ctx.json(photoService.getAllPhotos());
  }

  public void getPhotoById(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    photoService
        .getPhotoById(id)
        .ifPresentOrElse(ctx::json, () -> ctx.status(404).result("Photo not found"));
  }

  public void createPhoto(Context ctx) {
    Photo myPhoto = ctx.bodyAsClass(Photo.class);
    ctx.status(201).json(photoService.createPhoto(myPhoto));
  }

  public void updatePhoto(Context ctx) {
    Photo myPhoto = ctx.bodyAsClass(Photo.class);
    photoService
        .updatePhoto(myPhoto)
        .ifPresentOrElse(
            updatedPhoto -> ctx.status(200).json(updatedPhoto),
            () -> ctx.status(404).result("Photo not found"));
  }

  public void listarPhotos(@NotNull Context ctx) {
    List<Photo> photos = photoService.getAllPhotos();

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("titulo", "Lista de Fotos");
    modelo.put("photos", photos);

    ctx.render("/templates/listar.html", modelo);
  }

  public void procesarPhotos(@NotNull Context ctx) {
    ctx.uploadedFiles("photo")
        .forEach(
            uploadedFile -> {
              try {
                byte[] bytes = uploadedFile.content().readAllBytes();
                String encodedString = Base64.getEncoder().encodeToString(bytes);

                Photo photo =
                    new Photo(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
                photoService.createPhoto(photo);
              } catch (IOException e) {
                e.printStackTrace();
                ctx.status(500).result("Error processing photo upload.");
              }
            });

    ctx.redirect(Routes.PHOTOLIST.getPath());
  }

  public void visualizarPhotos(@NotNull Context ctx) {
    try {
      Long id = ctx.pathParamAsClass("id", Long.class).get();

      photoService
          .getPhotoById(id)
          .ifPresentOrElse(
              photo -> {
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("photo", photo);
                ctx.render("/templates/visualizar.html", modelo);
              },
              () -> ctx.redirect(Routes.PHOTOLIST.getPath()));
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      ctx.redirect(Routes.PHOTOLIST.getPath());
    }
  }

  public void eliminarPhotos(@NotNull Context ctx) {
    try {
      Long id = ctx.pathParamAsClass("id", Long.class).get();

      if (photoService.deletePhoto(id)) {
        ctx.redirect(Routes.PHOTOLIST.getPath());
      } else {
        ctx.status(404).result("Photo not found");
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      ctx.redirect(Routes.PHOTOLIST.getPath());
    }
  }
}
