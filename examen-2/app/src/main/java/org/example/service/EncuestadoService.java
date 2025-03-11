package org.example.service;

import org.example.model.Encuestado;
import org.example.repository.EncuestadoRepository;
import org.example.util.enums.NivelEscolar;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class EncuestadoService {

    private final EncuestadoRepository encuestadoRepository;

    public EncuestadoService(EntityManager entityManager) {
        this.encuestadoRepository = new EncuestadoRepository(entityManager);
    }


    public Optional<Encuestado> findById(Long id) {
        return encuestadoRepository.findById(id);
    }


    public List<Encuestado> findAll() {
        return encuestadoRepository.findAll();
    }


    public Optional<Encuestado> createEncuestado(String nombre, String sector, NivelEscolar nivelEscolar) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (sector == null || sector.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector cannot be empty.");
        }
        if (nivelEscolar == null) {
            throw new IllegalArgumentException("NivelEscolar cannot be empty.");
        }

        Encuestado encuestado = new Encuestado();
        encuestado.setNombre(nombre);
        encuestado.setSector(sector);
        encuestado.setNivelEscolar(nivelEscolar);

        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        return Optional.of(encuestadoRepository.save(encuestado));
    }


    public Optional<Encuestado> updateEncuestado(Long id, String nombre, String sector, NivelEscolar nivelEscolar) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (sector == null || sector.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector cannot be empty.");
        }
        if (nivelEscolar == null) {
            throw new IllegalArgumentException("NivelEscolar cannot be empty.");
        }

        Encuestado encuestado = encuestadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Respondant not found. ID: " + id));

        encuestado.setNombre(nombre);
        encuestado.setSector(sector);
        encuestado.setNivelEscolar(nivelEscolar);

        return Optional.of(encuestadoRepository.update(encuestado));
    }


    public void deleteEncuestado(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        encuestadoRepository.deleteById(id);
    }
}
