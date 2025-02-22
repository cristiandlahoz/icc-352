package org.example.services;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return comments.values().stream().sorted(Comparator.comparing(Comment::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Comment getCommentById(Long commentId) {
        if (commentId == null) {
            throw new IllegalArgumentException("Argument commentId cannot be null");
        } else if (!comments.containsKey(commentId))
            throw new NotFoundException("User not found");

        return comments.get(commentId);
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        if (articleId == null) {
            throw new IllegalArgumentException("Argument articleId cannot be null");
        } else
            return comments.values().stream()
                    .filter(comment -> comment.getArticleId().equals(articleId))
                    .sorted(Comparator.comparing(Comment::getDate).reversed())
                    .collect(Collectors.toList());
    }

    public Comment createComment(String author, String comment, Long articleId) {
        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            throw new NotFoundException("Article not found with ID " + articleId);
        }
	    return new Comment(comment, author, article);
    }

    public void updateComment(Comment comment) {
        if (comment == null)
            throw new IllegalArgumentException("Comment cannot be null");
        else if (!comments.containsKey(comment.getCommentId()))
            throw new NotFoundException("Comment cannot be found");
        else {
            Comment myComment = comments.get(comment.getCommentId());
            myComment.setComment(comment.getComment());
            comments.put(myComment.getCommentId(), myComment);
        }
    }

    public void deleteCommentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        } else if (!comments.containsKey(id)) {
            throw new NotFoundException("Comment cannot be found");
        } else
            comments.remove(id);
    }

    public Comment getCommentByArticleAndCommentId(Long articleId, Long commentId) {
        if (articleId == null) {
            throw new IllegalArgumentException("Argument articleId cannot be null");
        } else if (commentId == null) {
            throw new IllegalArgumentException("Argument commentId cannot be null");
        } else if (!comments.containsKey(commentId)) {
            throw new NotFoundException("Selected commentId not found exception");
        } else if (!comments.get(commentId).getArticleId().equals(articleId)) {
            throw new NotFoundException("There is not comment with that articleId and commentId");
        } else {
            return comments.get(commentId);
        }
    }

}
