package org.example.repository;

import java.util.Optional;

import org.example.model.Location;
import org.example.util.baseclasses.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class LocationRepository extends BaseRepository<Location, Long> {
  public LocationRepository(EntityManager entityManager) {
    super(entityManager, Location.class);
  }

  public Optional<Location> getLocationByCoordinates(Double latitude, Double longitude) {
    EntityTransaction transaction = this.entityManager.getTransaction();

    try {
      transaction.begin();
      Optional<Location> location = this.entityManager
          .createQuery("SELECT l FROM Location l WHERE l.latitude = :latitude AND l.longitude = :longitude",
              Location.class)
          .setParameter("latitude", latitude)
          .setParameter("longitude", longitude)
          .getResultList()
          .stream()
          .findFirst();
      transaction.commit();
      return location;
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException(e);
    }
  }
}
