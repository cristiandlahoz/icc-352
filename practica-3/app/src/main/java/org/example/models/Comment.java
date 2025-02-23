package org.example.models;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  private String comment;
  private String authorUsername;
  private Date date;

  @ManyToOne
  @JoinColumn(name = "article_id", nullable = false)
  private Article article;

  @PrePersist
  protected void onCreate() {
    this.date = new Date();
  }

  public Comment(String comment, String authorUsername, Article article) {
    this.comment = comment;
    this.authorUsername = authorUsername;
    this.article = article;
  }
}
