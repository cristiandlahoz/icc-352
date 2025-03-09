package org.example.repository;

import org.example.model.Encuestado;
import org.example.util.baseclasses.BaseRepository;


import jakarta.persistence.EntityManager;

public class EncuestadoRepository extends BaseRepository<Encuestado, Long> {
    public EncuestadoRepository(EntityManager entityManager) {
        super(entityManager, Encuestado.class);
    }


}

