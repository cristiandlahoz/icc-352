package org.example.controllers;

import io.javalin.http.Context;

import java.util.Collection;

import org.example.models.Tag;
import org.example.services.TagService;

public class TagController {
    private static final TagService tagService = new TagService();

    public static Collection<Tag> getAllTags() {
        return TagService.getAllTags();
    }

    public static Tag getTagById(Context ctx) {
        String stringId = ctx.pathParam("id");
        Long id = Long.parseLong(stringId);
        return tagService.getTagById(id);
        
    }

    public static void createTag(Context ctx) {
        Tag myTag = ctx.bodyAsClass(Tag.class);
        tagService.createTag(myTag.getName());
        ctx.status(201);
    }

    public static void  updateTag(Context ctx) {
        Tag myTag = ctx.bodyAsClass(Tag.class);
        tagService.updateTag(myTag);
        ctx.status(200);
    }

    public static void deleteTag(Context ctx) {
        String stringId = ctx.pathParam("id");
        Long id = Long.parseLong(stringId);
        tagService.deleteTagById(id);
        ctx.status(200);
    }
}
