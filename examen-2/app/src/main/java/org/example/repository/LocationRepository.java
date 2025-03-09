package org.example.repository;

import org.example.model.Location;
import org.example.util.baseclasses.BaseRepository;


import jakarta.persistence.EntityManager;

public class LocationRepository extends BaseRepository<Location, Long> {
    public LocationRepository(EntityManager entityManager) {
        super(entityManager, Location.class);
    }
}
