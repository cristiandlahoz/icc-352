package org.example.models;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Photo implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;
  private String mimeType;
  @Lob private String fotoBase64;

  public Photo(String nombre, String mimeType, String fotoBase64) {
    this.nombre = nombre;
    this.mimeType = mimeType;
    this.fotoBase64 = fotoBase64;
  }
}
