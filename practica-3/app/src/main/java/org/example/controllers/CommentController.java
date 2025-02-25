package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Objects;
import org.example.models.Comment;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.util.BaseController;
import org.example.util.Routes;

public class CommentController extends BaseController {
  private final CommentService commentService;
  private final ArticleService articleService;

  public CommentController(
      Javalin app, CommentService commentService, ArticleService articleService) {
    super(app);
    this.commentService = commentService;
    this.articleService = articleService;
  }

  @Override
  public void applyRoutes() {
    app.post(Routes.COMMENTS.getPath(), this::createComment);
    app.get(Routes.COMMENTS.getPath(), this::getAllComments);
    app.get(Routes.COMMENT.getPath(), this::getCommentById);
    app.put(Routes.COMMENT.getPath(), this::updateComment);
    app.delete(Routes.COMMENT.getPath(), this::deleteComment);
  }

  public void getAllComments(Context ctx) {
    ctx.json(commentService.getAllComments());
  }

  public void getCommentById(Context ctx) {
    String stringId = ctx.pathParam("id");
    Long id = Long.parseLong(stringId);
    Comment myComment = commentService.getCommentById(id).orElseGet(Comment::new);
    ctx.status(200).json(myComment);
  }

  public void createComment(Context ctx) {
    Long articleId = Long.parseLong(Objects.requireNonNull(ctx.formParam("articleId")));
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
    if (articleService.getArticleById(articleId) == null) {
      ctx.status(400).result("articleId cannot be null");
      return;
    }

    Comment myComment = commentService.createComment(author, comment, articleId);
    ctx.json(myComment);
  }

  public void updateComment(Context ctx) {
    Comment myComment = ctx.bodyAsClass(Comment.class);
    commentService.updateComment(myComment);
    ctx.status(200);
  }

  public void deleteComment(Context ctx) {
    String stringId = ctx.pathParam("id");
    Long id = Long.parseLong(stringId);
    commentService.deleteCommentById(id);
    ctx.status(200);
  }
}
