package org.example.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.exceptions.NotFoundException;
import org.example.models.Comment;

public class CommentService {
    private static final Map<Long, Comment> comments = new HashMap<>();

    public CommentService() {
    }

    static {
    }

    public Collection<Comment> getAllComments() {
        return comments.values();
    }

    public Comment getCommentById(Long commentId) {
        if (commentId == null) {
            throw new IllegalArgumentException("Argument commentId cannot be null");
        } else if (!comments.containsKey(commentId))
            throw new NotFoundException("User not found");

        return comments.get(commentId);
    }

    public void createComment(Comment comment) {
        comments.put(comment.getCommentId(), comment);
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

    public List<Comment> getCommentsByArticleId(Long articleId) {
        if (articleId == null) {
            throw new IllegalArgumentException("Argument articleId cannot be null");
        } else
            return comments.values().stream()
                    .filter(comment -> comment.getArticleId().equals(articleId))
                    .collect(Collectors.toList());
    }

    public Comment getCommentByArticleAndCommentId(Long articleId, Long commentId) {
        if (articleId == null) {
            throw new IllegalArgumentException("Argument articleId cannot be null");
        }else if (commentId == null) {
            throw new IllegalArgumentException("Argument commentId cannot be null");
        }else if(!comments.containsKey(commentId)){
            throw new NotFoundException("Selected commentId not found exception");
        } else if(!comments.get(commentId).getArticleId().equals(articleId)){
            throw new NotFoundException("There is not comment with that articleId and commentId");
        }else{
           return comments.get(commentId);
       }
    }

}
