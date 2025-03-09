package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import org.example.util.enums.NivelEscolar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "encuestados")
public class Encuestado implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long encuestadoId;

  @Column(nullable = false)
  // @NotBlank(message = "El nombre no puede estar vacío")
  // @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
  private String nombre;

  @Column(nullable = false)
  // @NotBlank(message = "El sector no puede estar vacío")
  // @Size(max = 100, message = "El sector no puede exceder los 100 caracteres")
  private String sector;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NivelEscolar nivelEscolar;

  public Encuestado(String nombre, String sector, NivelEscolar nivelEscolar) {
    this.nombre = nombre;
    this.sector = sector;
    this.nivelEscolar = nivelEscolar;
  }
}
