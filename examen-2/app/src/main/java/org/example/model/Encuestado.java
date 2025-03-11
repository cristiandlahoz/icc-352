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
  private String nombre;

  @Column(nullable = false)
  private String sector;

  @OneToOne(mappedBy = "encuestado")
  private Form form;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NivelEscolar nivelEscolar;

  public Encuestado(String nombre, String sector, NivelEscolar nivelEscolar) {
    this.nombre = nombre;
    this.sector = sector;
    this.nivelEscolar = nivelEscolar;
  }
}
