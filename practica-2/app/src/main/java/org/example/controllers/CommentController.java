package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Comment;
import org.example.models.Tag;
import org.example.services.CommentService;


public class CommentController {
    private static final CommentService commentService = new CommentService();

    public static void getAllComments(Context ctx) {
        ctx.json(commentService);
    }

    public static void getCommentById(Context ctx) {
        String stringId = ctx.pathParam("id");
        Long id = Long.parseLong(stringId);
        Comment myComment = commentService.getCommentById(id);
        ctx.status(200).json(myComment);
    }

    public static void createComment(Context ctx) {
        Comment myComment = ctx.bodyAsClass(Comment.class);
        commentService.createComment(myComment);
        ctx.status(201);
    }


    public static void updateComment(Context ctx) {
        Comment myComment = ctx.bodyAsClass(Comment.class);
        commentService.updateComment(myComment);
        ctx.status(200);
    }

    public static void deleteComment(Context ctx) {
        String stringId = ctx.pathParam("id");
        Long id = Long.parseLong(stringId);
        commentService.deleteCommentById(id);
        ctx.status(200);
    }

    public static void getCommentByArticleAndCommentId(Context ctx) {
        try {
            Long articleId = Long.parseLong(ctx.pathParam("articleId"));
            Long commentId = Long.parseLong(ctx.pathParam("commentId"));

            Comment comment = commentService.getCommentByArticleAndCommentId(articleId, commentId);

            ctx.status(200).json(comment);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(e.getMessage());
        } catch (Exception e) {
            ctx.status(404).json(e.getMessage());
        }
    }

}
