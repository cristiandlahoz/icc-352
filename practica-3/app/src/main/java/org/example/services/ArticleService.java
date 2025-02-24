package org.example.services;

import java.util.*;
import java.util.stream.Collectors;
import org.example.exceptions.NotFoundException;
import org.example.models.Article;
import org.example.models.User;
import org.example.repository.ArticleRepository;

public class ArticleService {
  private final ArticleRepository articleRepository;
  private final UserService userService;

  public ArticleService(ArticleRepository articleRepository, UserService userService) {
    this.articleRepository = articleRepository;
    this.userService = userService;
  }

  public List<Article> getAllArticles() {
    return articleRepository.findAll().stream()
        .sorted(Comparator.comparing(Article::getDate).reversed())
        .collect(Collectors.toList());
  }

  public List<Article> getAllArticles(int page, int pageSize) {
    return articleRepository.findAll(page, pageSize);
  }

  public List<Article> getAllArticlesByTag(int page, int pageSize, String tag) {
    return articleRepository.findAllByTagName(page, pageSize, tag);
  }

  public Long countAllArticles() {
    return articleRepository.countAll();
  }

  public Long countAllArticlesByTag(String tag) {
    return articleRepository.countAllByTagName(tag);
  }

  public Article getArticleById(Long articleId) {
    if (articleId == null) {
      throw new IllegalArgumentException("Article ID cannot be null");
    }
    return articleRepository
        .findById(articleId)
        .orElseThrow(() -> new NotFoundException("Article not found"));
  }

  public Article createArticle(String title, String content, String authorUsername) {
    if (title == null || content == null || authorUsername == null) {
      throw new IllegalArgumentException("Title, content, and author username cannot be null");
    }
    boolean exists =
        articleRepository.findAll().stream()
            .anyMatch(article -> article.getTitle().equalsIgnoreCase(title));

    if (exists) {
      throw new IllegalArgumentException("An article with this title already exists");
    }

    Optional<User> authorOpt = userService.getUserByUsername(authorUsername);

    if (authorOpt.isEmpty()) {
      throw new IllegalArgumentException("User not found with username: " + authorUsername);
    }

    Article newArticle = new Article(title, content, authorOpt.get());
    return articleRepository.save(newArticle);
  }

  public Optional<Article> updateArticle(Article article) {
    if (article == null || article.getArticleId() == null) {
      throw new IllegalArgumentException("Article and ID cannot be null");
    }
    Optional<Article> existingArticle = articleRepository.findById(article.getArticleId());
    if (existingArticle.isEmpty()) {
      throw new IllegalArgumentException("Article not found");
    }

    try {
      return Optional.ofNullable(articleRepository.update(article));
    } catch (Exception e) {
      System.out.println(
          "Error while updating article, article with this title already exists: "
              + article.getTitle());
    }
    return Optional.empty();
  }

  public List<Article> getArticleByAuthor(String author) {
    return articleRepository.findAll().stream()
        .filter(article -> article.getAuthor().equals(author))
        .sorted(Comparator.comparing(Article::getDate).reversed())
        .collect(Collectors.toList());
  }

  public void deleteArticleById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    articleRepository.deleteById(id);
  }
}
