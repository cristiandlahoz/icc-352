package org.example.model;

import lombok.*;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "locations")
public class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Double latitude;
  @Column(nullable = false)
  private Double longitude;

  @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
  private List<Form> form;

  public Location(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
