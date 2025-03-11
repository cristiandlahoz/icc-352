package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.*;
import org.example.util.enums.Role;

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

  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<Form> forms;

  public User(String username, String password, String name, Role role) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.role = role;
  }
}
