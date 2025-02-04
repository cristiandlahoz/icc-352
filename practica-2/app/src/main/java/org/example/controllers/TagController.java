package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagController {
    private static List<Tag> tags = new ArrayList<>();

    public static void getAllTags(Context ctx) {
        ctx.json(tags);
    }

    public static void createTag(Context ctx) {
        Tag tag = ctx.bodyAsClass(Tag.class);
        tags.add(tag);
        ctx.status(201);
    }
}
