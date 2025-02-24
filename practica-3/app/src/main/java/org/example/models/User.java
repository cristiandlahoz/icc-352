package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.util.AccessStatus;
import org.example.util.Role;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private AccessStatus accessStatus;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "photo_id", referencedColumnName = "id", nullable = true)
  private Photo profilePhoto;

  public User(String username, String name, String password, Role role, AccessStatus accessStatus, Photo profilePhoto) {
    this.username = username;
    this.name = name;
    this.password = password;
    this.role = role;
    this.accessStatus = accessStatus;
    this.profilePhoto = profilePhoto;
  }
}
