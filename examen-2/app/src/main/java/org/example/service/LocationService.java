package org.example.service;

import org.example.model.Location;
import org.example.repository.LocationRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(EntityManager entityManager) {
        this.locationRepository = new LocationRepository(entityManager);
    }

    public Optional<Location> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        return locationRepository.findById(id);
    }

    // Obtener todas las ubicaciones
    public List<Location> findAll() {
        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            throw new IllegalArgumentException("Location not found.");
        }
        return locations;
    }

    // Crear una nueva ubicación
    public Optional<Location> createLocation(Double latitude, Double longitude) {
        if (latitude == null ) {
            throw new IllegalArgumentException("Latitud cannot be null.");
        }
        if (longitude == null ) {
            throw new IllegalArgumentException("Longitud cannot be null.");
        }

        Location location = new Location(latitude, longitude);
        return Optional.of(locationRepository.save(location));
    }

    // Actualizar una ubicación existente
    public Optional<Location> updateLocation(Long id, Double latitude, Double longitude) {
        if (id == null ) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        if (latitude == null ) {
            throw new IllegalArgumentException("Latitud cannot be null.");
        }
        if (longitude == null ) {
            throw new IllegalArgumentException("Longitud cannot be null.");
        }

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada con ID: " + id));

        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return Optional.of(locationRepository.update(location));
    }

    // Eliminar una ubicación por ID
    public void deleteLocation(Long id) {
        if (id == null ) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        locationRepository.deleteById(id);
    }
}
