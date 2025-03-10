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

  private Boolean isSynchronized;

  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date(System.currentTimeMillis());
  }

  public Form(User user, Location location, Boolean isSynchronized) {
    this.location = location;
    this.user = user;
    this.isSynchronized = isSynchronized;
  }

}
