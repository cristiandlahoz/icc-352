package org.example.util;

import lombok.Getter;

@Getter
public enum PageSize {
  SMALL(5),
  MEDIUM(10),
  LARGE(20),
  DEFAULT(5);

  private int size;

  PageSize(int size) {
    this.size = size;
  }
}
