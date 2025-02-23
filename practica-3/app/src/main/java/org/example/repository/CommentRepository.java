package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.models.Comment;
import org.example.util.BaseRepository;

public class CommentRepository extends BaseRepository<Comment, Long> {
  public CommentRepository(EntityManager entityManager) {
    super(entityManager, Comment.class);
  }
}
