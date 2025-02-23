package org.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.example.repository.*;
import org.example.services.*;

public class DependencyConfig {
  private static EntityManagerFactory entityManagerFactory;
  private static EntityManager entityManager;

  private static ArticleRepository articleRepository;
  private static CommentRepository commentRepository;
  private static TagRepository tagRepository;
  private static UserRepository userRepository;

  @Getter private static ArticleService articleService;
  @Getter private static AuthService authService;
  @Getter private static CommentService commentService;
  @Getter private static TagService tagService;
  @Getter private static UserService userService;

  public static void init() {
    entityManagerFactory = Persistence.createEntityManagerFactory("h2-persistence-unit");
    entityManager = entityManagerFactory.createEntityManager();

    articleRepository = new ArticleRepository(entityManager);
    commentRepository = new CommentRepository(entityManager);
    tagRepository = new TagRepository(entityManager);
    userRepository = new UserRepository(entityManager);

    articleService = new ArticleService(articleRepository);
    authService = new AuthService(userRepository);
    commentService = new CommentService(commentRepository, articleService);
    tagService = new TagService(tagRepository);
    userService = new UserService(userRepository);
  }
}
