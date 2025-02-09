package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.models.Comment;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.util.BaseController;

public class CommentController extends BaseController {
    private static final CommentService commentService = new CommentService();

    public CommentController(Javalin app) {
        super(app);

    }

    @Override
    public void applyRoutes() {
        app.post("/comments", CommentController::createComment);
    }

    public static void getAllComments(Context ctx) {
        ctx.json(commentService.getAllComments());
    }

    public static void getCommentById(Context ctx) {
        String stringId = ctx.pathParam("id");
        Long id = Long.parseLong(stringId);
        Comment myComment = commentService.getCommentById(id);
        ctx.status(200).json(myComment);
    }

    public static void createComment(Context ctx) {
        Long articleId = Long.parseLong(ctx.formParam("articleId"));
        String author = ctx.formParam("author");
        String comment = ctx.formParam("comment");

        if (comment == null || comment.trim().isEmpty()) {
            ctx.status(400).result("Comment cannot be blanck");
            return;
        }
        if (author == null || author.trim().isEmpty()) {
            ctx.status(400).result("Username cannot be blanck");
            return;
        }
        if (new ArticleService().getArticleById(articleId) == null) {
            ctx.status(400).result("articleId cannot be null");
            return;
        }

        Comment myComment = new Comment(comment, author, articleId);
        commentService.createComment(myComment);
        ctx.json(myComment);
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
