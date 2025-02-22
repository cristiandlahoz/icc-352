package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.models.Tag;
import org.example.util.BaseRepository;

public class TagRepository extends BaseRepository<Tag, Long> {
	public TagRepository(EntityManager entityManager) {
		super(entityManager, Tag.class);
	}
}
