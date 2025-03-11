package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.model.Encuestado;
import org.example.model.Form;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.FormRepository;

public class FormService {
  private FormRepository formRepository;

  public FormService(FormRepository formRepository) {
    this.formRepository = formRepository;
  }

  public List<Form> getAllForms() {
    return formRepository.findAll();
  }

  public Optional<Form> getFormById(Long id) {
    if (id == null) return Optional.empty();
    return formRepository.findById(id);
  }

  public Optional<Form> createForm(
      User user, Location location, Encuestado encuestado, Boolean isSynchronized) {
    if (user == null || location == null || isSynchronized == null || encuestado == null)
      throw new IllegalArgumentException(
          "User, location, encuestado and isSynchronized must not be null");
    else if (user.getUserId() == null
        || location.getLocationId() == null
        || encuestado.getEncuestadoId() == null)
      throw new IllegalArgumentException("User, location and encuestado must have valid IDs");

    Form form = new Form(user, location, encuestado, isSynchronized);
    return Optional.ofNullable(formRepository.save(form));
  }

  public Optional<Form> updateForm(Long formId, Boolean isSynchronized) {
    if (formId == null || isSynchronized == null)
      throw new IllegalArgumentException("Form ID and isSynchronized must not be null");

    if (!isSynchronized) throw new IllegalArgumentException("Form cannot be unsynchronized");

    Optional<Form> form =
        formRepository
            .findById(formId)
            .map(
                f -> {
                  f.setIsSynchronized(isSynchronized);
                  return f;
                });
    return Optional.ofNullable(formRepository.update(form.get()));
  }

  public void deleteForm(Long id) {
    if (id == null) throw new IllegalArgumentException("Form ID must not be null");
    else if (getFormById(id).isEmpty()) throw new IllegalArgumentException("Form not found");
    formRepository.deleteById(id);
  }
}
