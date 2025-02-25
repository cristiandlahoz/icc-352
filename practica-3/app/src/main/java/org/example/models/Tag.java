package org.example.models;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;

  @Column(unique = true, nullable = false)
  private String name;

  public Tag(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
