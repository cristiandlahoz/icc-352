package org.example.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Form {
  private Long formId;
  private User user;
  private Location location;
  private Boolean isSynchronized;
}
