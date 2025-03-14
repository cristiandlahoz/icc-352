package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Encuestado;
import org.example.util.baseclasses.BaseRepository;

public class EncuestadoRepository extends BaseRepository<Encuestado, Long> {
  public EncuestadoRepository(EntityManager entityManager) {
    super(entityManager, Encuestado.class);
  }
}
