package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.example.models.Article;
import org.example.util.BaseRepository;

public class ArticleRepository extends BaseRepository<Article, Long> {
  public ArticleRepository(EntityManager entityManager) {
    super(entityManager, Article.class);
  }

  public List<Article> findAll(int page, int pageSize) {
    String jpql = "SELECT a FROM Article a ORDER BY a.date DESC";
    TypedQuery<Article> query = entityManager.createQuery(jpql, Article.class);
    query.setFirstResult((page - 1) * pageSize);
    query.setMaxResults(pageSize);
    return query.getResultList();
  }

  public List<Article> findAllByTagName(int page, int pageSize, String tagName) {
    String jpql =
        "SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName ORDER BY a.date DESC";
    TypedQuery<Article> query = entityManager.createQuery(jpql, Article.class);
    query.setParameter("tagName", tagName);
    query.setFirstResult((page - 1) * pageSize);
    query.setMaxResults(pageSize);
    return query.getResultList();
  }

  public Long countAll() {
    String jpql = "SELECT COUNT(a) FROM Article a";
    return entityManager.createQuery(jpql, Long.class).getSingleResult();
  }

  public Long countAllByTagName(String tagName) {
    String jpql = "SELECT COUNT(a) FROM Article a JOIN a.tags t WHERE t.name = :tagName";
    return entityManager
        .createQuery(jpql, Long.class)
        .setParameter("tagName", tagName)
        .getSingleResult();
  }
}
