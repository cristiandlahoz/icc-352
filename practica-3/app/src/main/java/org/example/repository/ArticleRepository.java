package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.models.Article;
import org.example.util.BaseRepository;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository extends BaseRepository<Article, Long> {
	public ArticleRepository(EntityManager entityManager) {
		super(entityManager, Article.class);
	}

	public List<Article> findAll(int page, int pageSize){
			String jpql = "SELECT a FROM Article a ORDER BY a.date DESC";
			TypedQuery<Article> query = entityManager.createQuery(jpql, Article.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
	}

	public long countAll(){
		String jpql = "SELECT COUNT(a) FROM Article a";
		return entityManager.createQuery(jpql, Long.class).getSingleResult();
	}
}
