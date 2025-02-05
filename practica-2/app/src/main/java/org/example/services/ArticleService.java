package org.example.services;

import org.example.models.Article;
import org.example.models.User;

import java.util.*;
public class ArticleService {

    private static final Map<Long, Article> articles = new HashMap<>();
    public ArticleService() {

    }

    static {
        User user1 = new User("john_doe", "John Doe", "password123", true, false);
        User user2 = new User("jane_doe", "Jane Doe", "securePass456", false, false);

        articles.put(1L, new Article(1L, "Title 1", "Content 1", user1, new Date()));
        articles.put(2L, new Article(2L, "Title 2", "Content 2", user2, new Date()));
        articles.put(3L, new Article(3L, "Title 3", "Content 3", user1, new Date()));
        articles.put(4L, new Article(4L, "Title 4", "Content 4", user2, new Date()));
        articles.put(5L, new Article(5L, "Title 5", "Content 5", user1, new Date()));
        articles.put(6L, new Article(6L, "Title 6", "Content 6", user2, new Date()));
        articles.put(7L, new Article(7L, "Title 7", "Content 7", user1, new Date()));
        articles.put(8L, new Article(8L, "Title 8", "Content 8", user2, new Date()));
    }

    public Collection<Article> getAllArticles() {
        return articles.values(); // Retorna una copia para evitar modificaciones directas
    }

    public Article getArticleById(Long articleId) {
        if (articleId == null) {
            throw new IllegalArgumentException("tagId cannot be null");
        }
        return articles.get(articleId);
    }

    public void createArticle(Article article) {
        articles.put(article.getArticleId(), article);
    }

    public void updateArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article cannot be null");
        }
        else if (!articles.containsKey(article.getArticleId())) {
            throw new IllegalArgumentException("Article not found");
        }else{
            Article myArticle =articles.get(article.getArticleId());
            myArticle.setArticleTitle(article.getArticleTitle());
            myArticle.setArticleContent(article.getArticleContent());
            articles.put(myArticle.getArticleId(), myArticle);
        }
    }

    public void deleteArticleById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        else if (!articles.containsKey(id)) {
            throw new IllegalArgumentException("Article not found");
        }else{
            articles.remove(id);
        }
    }

}

