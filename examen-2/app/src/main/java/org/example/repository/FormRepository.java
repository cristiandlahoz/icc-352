package org.example.repository;

import org.example.model.Form;
import jakarta.persistence.EntityManager;
import org.example.util.baseclasses.BaseRepository;

public class FormRepository extends BaseRepository<Form, Long> {
  public FormRepository(EntityManager entityManager) {
    super(entityManager, Form.class);
  }
}
