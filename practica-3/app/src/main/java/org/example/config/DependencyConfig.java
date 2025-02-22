package org.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.repository.*;
import org.example.services.*;

public class DependencyConfig {
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;

	private static ArticleRepository articleRepository;
	private static CommentRepository commentRepository;
	private static TagRepository tagRepository;
	private static UserRepository userRepository;

	private static ArticleService articleService;
	private static CommentService commentService;
	private static TagService tagService;
	private static UserService userService;

	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("h2-persistence-unit");
		entityManager = entityManagerFactory.createEntityManager();

		articleRepository = new ArticleRepository(entityManager);
		commentRepository = new CommentRepository(entityManager);
		tagRepository = new TagRepository(entityManager);
		userRepository = new UserRepository(entityManager);
		
		

	}
}
