package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.models.Tag;
import org.example.util.BaseRepository;

public class TagRepository extends BaseRepository<Tag, Long> {
  public TagRepository(EntityManager entityManager) {
    super(entityManager, Tag.class);
  }

  public Tag findByName(String name) {
    String jpql = "SELECT t FROM Tag t WHERE t.name = :name";
    TypedQuery<Tag> query = entityManager.createQuery(jpql, Tag.class);
    query.setParameter("name", name);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }
}
