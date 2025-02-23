package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Collection;
import org.example.models.Tag;
import org.example.services.TagService;
import org.example.util.BaseController;
import org.example.util.Routes;

public class TagController extends BaseController {
  private final TagService tagService;

  public TagController(Javalin app, TagService tagService) {
    super(app);
    this.tagService = tagService;
  }

  @Override
  public void applyRoutes() {
    app.post(Routes.TAGS.getPath(), this::createTag);
  }

  public Collection<Tag> getAllTags() {
    return tagService.getAllTags();
  }

  public Tag getTagById(Context ctx) {
    String stringId = ctx.pathParam("id");
    Long id = Long.parseLong(stringId);
    return tagService.getTagById(id).orElse(null);
  }

  public void createTag(Context ctx) {
    Tag myTag = ctx.bodyAsClass(Tag.class);
    tagService.createTag(myTag.getName());
    ctx.status(201);
  }

  public void updateTag(Context ctx) {
    Tag myTag = ctx.bodyAsClass(Tag.class);
    tagService.updateTag(myTag);
    ctx.status(200);
  }

  public void deleteTag(Context ctx) {
    String stringId = ctx.pathParam("id");
    Long id = Long.parseLong(stringId);
    tagService.deleteTagById(id);
    ctx.status(200);
  }
}
