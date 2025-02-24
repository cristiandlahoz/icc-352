package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.models.Photo;
import org.example.util.BaseRepository;

public class PhotoRepository extends BaseRepository<Photo, Long> {
  public PhotoRepository(EntityManager entityManager) {
    super(entityManager, Photo.class);
  }
}
