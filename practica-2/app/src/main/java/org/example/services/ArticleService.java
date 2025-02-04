package org.example.services;

import org.example.models.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleService {

    private static final List<Article> articles = new ArrayList<>();

    static {
        // Inicialización de datos de prueba
    }

    public List<Article> getAllArticles() {
        return new ArrayList<>(articles); // Retorna una copia para evitar modificaciones directas
    }

    public void createArticle(Article article) {
        articles.add(article);
    }

    public Optional<Article> deleteArticleById(Long id) {
        Optional<Article> article = articles.stream().filter(a -> a.getArticleId().equals(id)).findFirst();
        article.ifPresent(articles::remove);
        return article;
    }
}

