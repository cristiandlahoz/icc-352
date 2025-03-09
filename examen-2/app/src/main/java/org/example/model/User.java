package org.example.model;

import lombok.*;
import org.example.util.enums.Role;

@Getter
@Setter
@NoArgsConstructor
public class User {
  private Long userId;
  private String name;
  private String username;
  private String password;
  private Role role;
}
