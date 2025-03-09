package org.example.model;

import java.io.Serializable;
import java.util.List;

import org.example.util.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  @Column(unique = true)
  private String username;
  private String password;
  private String name;
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Form> forms;

  public User(String username, String password, String name, Role role) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.role = role;
  }
}
