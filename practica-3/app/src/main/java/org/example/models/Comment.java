package org.example.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  private String comment;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private Date date;

  @ManyToOne
  @JoinColumn(name = "article_id", nullable = false)
  @JsonBackReference
  private Article article;

  @PrePersist
  protected void onCreate() {
    this.date = new Date();
  }

  public Comment(String comment, User user, Article article) {
    this.comment = comment;
    this.user = user;
    this.article = article;
  }

  public String getPrettyTime() {
    PrettyTime pt = new PrettyTime();
    return pt.format(date);
  }
}
