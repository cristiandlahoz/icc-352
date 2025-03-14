package org.example.dto;

import org.example.model.Form;

public record DTOform(
        Long formId,
        String username,
        String fullname,
        String sector,
        String education,
        Double latitude,
        Double longitude,
        Boolean isSynchronized) {

    // Constructor que mapea directamente un Form a un FormDTO
    public DTOform(Form form) {
        this(
                form.getFormId(),
                form.getUser().getUsername(),
                form.getEncuestado() != null ? form.getEncuestado().getNombre() : "N/A",
                form.getEncuestado() != null ? form.getEncuestado().getSector() : "N/A",
                form.getEncuestado() != null ? form.getEncuestado().getNivelEscolar().toString() : "N/A",
                form.getLocation().getLatitude(),
                form.getLocation().getLongitude(),
                form.getIsSynchronized()
        );
    }
}
