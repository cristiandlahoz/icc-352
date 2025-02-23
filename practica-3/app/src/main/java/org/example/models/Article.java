package org.example.models;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long articleId;

  private String title;
  @Lob private String content;
  private String author;
  private Date date;

  @PrePersist
  protected void onCreate() {
    this.date = new Date();
  }

  @OneToMany(mappedBy = "article")
  private List<Comment> comments;

  @ManyToMany
  @JoinTable(
      name = "article_tags",
      joinColumns = @JoinColumn(name = "article_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  public Article(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }
}
