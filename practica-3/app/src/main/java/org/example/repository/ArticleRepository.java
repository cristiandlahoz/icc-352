package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.models.Article;
import org.example.util.BaseRepository;

public class ArticleRepository extends BaseRepository<Article, Long> {
	public ArticleRepository(EntityManager entityManager) {
		super(entityManager, Article.class);
	}
}
