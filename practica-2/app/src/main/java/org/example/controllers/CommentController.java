package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentController {
    private static List<Comment> comments = new ArrayList<>();

    public static void getAllComments(Context ctx) {
        ctx.json(comments);
    }

    public static void createComment(Context ctx) {
        Comment comment = ctx.bodyAsClass(Comment.class);
        comments.add(comment);
        ctx.status(201);
    }
}
