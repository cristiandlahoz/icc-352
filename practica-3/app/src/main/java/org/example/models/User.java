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

  @Column(unique = true)
  private String username;

  private String name;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private AccessStatus accessStatus;

  public User(String username, String name, String password, Role role, AccessStatus accessStatus) {
    this.username = username;
    this.name = name;
    this.password = password;
    this.role = role;
    this.accessStatus = accessStatus;
  }
}
