package org.wornux.urlshortener.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import io.javalin.security.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity("users")
@Getter
@Setter
@NoArgsConstructor
public class User {
  @Id
  private ObjectId id;
  private String username;
  private String password;
  private String email;
  private Roles role;
  private Date createdAt;
  private Date updatedAt;
  private boolean isDeleted;

  public User(String username, String password, String email, Roles role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
    this.isDeleted = false;
  }

  @PrePersist
  public void PrePersist() {
    if (createdAt == null) {
      createdAt = new Date();
    }
    updatedAt = new Date();
  }
}
