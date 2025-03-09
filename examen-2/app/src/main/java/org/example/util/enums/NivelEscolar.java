package org.example.util.enums;

public enum NivelEscolar {
  BASICO("Básico"),
  MEDIO("Medio"),
  GRADO_UNIVERSITARIO("Grado Universitario"),
  POSTGRADO("Postgrado"),
  DOCTORADO("Doctorado");

  private final String descripcion;

  NivelEscolar(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
