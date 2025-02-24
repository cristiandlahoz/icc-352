package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.util.BaseRepository;
import org.example.models.Photo;

public class PhotoRepository extends BaseRepository <Photo, Long>{
    public PhotoRepository(EntityManager entityManager) {
        super(entityManager, Photo.class);
    }
}
