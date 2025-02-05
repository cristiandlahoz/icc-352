package org.example.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.example.exceptions.NotFoundException;
import org.example.models.Comment;

public class CommentService {
    private static final Map<Long, Comment> comments = new HashMap<>();

    public CommentService() {
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

}
