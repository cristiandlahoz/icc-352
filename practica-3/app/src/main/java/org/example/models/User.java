package org.example.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.util.AccessStatus;
import org.example.util.Role;

import java.io.Serializable;
import java.util.List;

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

  private String name;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private AccessStatus accessStatus;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
  private List<Comment> comments;

  @OneToMany(mappedBy = "author")
  @JsonManagedReference
  private List<Article> articles;

  public User(String username, String name, String password, Role role, AccessStatus accessStatus) {
    this.username = username;
    this.name = name;
    this.password = password;
    this.role = role;
    this.accessStatus = accessStatus;
  }
}
