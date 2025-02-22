package org.example.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return articles.values().stream().sorted(Comparator.comparing(Article::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Article getArticleById(Long articleId) {
        if (articleId == null) {
            throw new IllegalArgumentException("tagId cannot be null");
        } else
            return articles.get(articleId);
    }

    public List<Article> getArticleByAuthor(String author) {
        return articles.values().stream().filter(article -> article.getAuthor().equals(author))
                .sorted(Comparator.comparing(Article::getDate).reversed()).collect(Collectors.toList());
    }

    public void createArticle(Article article) {
        articles.put(article.getArticleId(), article);
    }

    public void updateArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article cannot be null");
        } else if (!articles.containsKey(article.getArticleId())) {
            throw new IllegalArgumentException("Article not found");
        } else {
            Article myArticle = articles.get(article.getArticleId());
            myArticle.setTitle(article.getTitle());
            myArticle.setContent(article.getContent());
            myArticle.setTags(article.getTags());
            articles.put(myArticle.getArticleId(), myArticle);
        }
    }

    public void deleteArticleById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        } else if (!articles.containsKey(id)) {
            throw new IllegalArgumentException("Article not found");
        } else {
            articles.remove(id);
        }
    }

}
