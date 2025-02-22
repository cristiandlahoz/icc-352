package org.example.services;

import java.util.*;
import java.util.stream.Collectors;

import org.example.models.Article;
import org.example.models.Tag;
import org.example.repository.ArticleRepository;

public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public List<Article> getAllArticles() {
        return articleRepository.findAll().stream().sorted(Comparator.comparing(Article::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Article getArticleById(Long articleId) {
        if (articleId == null) {
            throw new IllegalArgumentException("Article ID cannot be null");
        }
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
    }


    public List<Article> getArticleByAuthor(String author) {
        return articleRepository.findAll().stream().filter(article -> article.getAuthor().equals(author))
                .sorted(Comparator.comparing(Article::getDate).reversed()).collect(Collectors.toList());
    }

    public Article createArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }
        return articleRepository.save(article);
    }

    public Article updateArticle(Article article) {
        if (article == null || article.getArticleId() == null) {
            throw new IllegalArgumentException("Article and ID cannot be null");
        }
        Optional<Article> existingArticle = articleRepository.findById(article.getArticleId());
        if (existingArticle.isEmpty()) {
            throw new IllegalArgumentException("Article not found");
        }
        return articleRepository.update(article);
    }

    public void deleteArticleById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        articleRepository.deleteById(id);
    }

}
