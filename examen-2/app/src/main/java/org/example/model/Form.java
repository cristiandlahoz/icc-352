package org.example.model;

import lombok.*;
import java.sql.Date;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "forms")
public class Form {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long formId;
  private Date createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToOne(fetch = FetchType.LAZY)
  @Column(nullable = false)
  private Encuestado encuestado;

  @Column(nullable = false)
  private Boolean isSynchronized;

  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date(System.currentTimeMillis());
  }

  public Form(User user, Location location, Encuestado encuestado, Boolean isSynchronized) {
    this.location = location;
    this.user = user;
    this.encuestado = encuestado;
    this.isSynchronized = isSynchronized;
  }

}
