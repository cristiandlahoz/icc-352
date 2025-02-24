package org.example.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long articleId;

  @Column(unique = true)
  private String title;

  @Lob private String content;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  private Date date;

  @PrePersist
  protected void onCreate() {
    this.date = new Date();
  }

  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Comment> comments;

  @ManyToMany
  @JoinTable(
      name = "article_tags",
      joinColumns = @JoinColumn(name = "article_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  public Article(String title, String content, User author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }
}
