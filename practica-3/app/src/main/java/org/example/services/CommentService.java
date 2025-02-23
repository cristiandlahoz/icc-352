package org.example.services;

import java.util.*;
import java.util.stream.Collectors;
import org.example.exceptions.NotFoundException;
import org.example.models.Article;
import org.example.models.Comment;
import org.example.repository.CommentRepository;

public class CommentService {
  private final CommentRepository commentRepository;
  private final ArticleService articleService;

  public CommentService(CommentRepository commentRepository, ArticleService articleService) {
    this.commentRepository = commentRepository;
    this.articleService = articleService;
  }

  public List<Comment> getAllComments() {
    return commentRepository.findAll().stream()
        .sorted(Comparator.comparing(Comment::getDate).reversed())
        .collect(Collectors.toList());
  }

  public Optional<Comment> getCommentById(Long commentId) {
    if (commentId == null) {
      throw new IllegalArgumentException("Comment ID cannot be null");
    }
    return commentRepository.findById(commentId);
  }

  public List<Comment> getCommentsByArticleId(Long articleId) {
    if (articleId == null) {
      throw new IllegalArgumentException("Article ID cannot be null");
    }
    return commentRepository.findAll().stream()
        .filter(comment -> comment.getArticle().getArticleId().equals(articleId))
        .sorted((c1, c2) -> c2.getDate().compareTo(c1.getDate()))
        .collect(Collectors.toList());
  }

  public Comment createComment(String author, String comment, Long articleId) {
    Article article = articleService.getArticleById(articleId);
    if (article == null) {
      throw new NotFoundException("Article not found with ID " + articleId);
    }
    return new Comment(comment, author, article);
  }

  public Comment updateComment(Comment comment) {
    if (comment == null || comment.getCommentId() == null) {
      throw new IllegalArgumentException("Comment and ID cannot be null");
    }
    Optional<Comment> existingComment = commentRepository.findById(comment.getCommentId());
    if (existingComment.isEmpty()) {
      throw new NotFoundException("Comment not found");
    }
    return commentRepository.update(comment);
  }

  public void deleteCommentById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Comment ID cannot be null");
    }
    commentRepository.deleteById(id);
  }
}
