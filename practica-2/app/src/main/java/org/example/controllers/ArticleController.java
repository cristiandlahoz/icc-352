package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.example.models.Article;
import org.example.models.Tag;
import org.example.services.ArticleService;
import org.example.util.BaseController;

import java.util.*;
import java.util.stream.Collectors;

public class ArticleController extends BaseController {
    private static final ArticleService articleService = new ArticleService();

    public ArticleController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.get("/", ctx -> {
            List<Article> articleCollection = articleService.getAllArticles().stream()
                    .sorted(Comparator.comparing(Article::getDate).reversed()).collect(Collectors.toList());
            Collection<Tag> tagCollection = TagController.getAllTags();
            Map<String, Object> model = setModel(
                    "title", "Wornux",
                    "articleCollection", articleCollection,
                    "tagCollection", tagCollection);

            ctx.render("/public/index.html", model);
        });

    };

    public static void getAllArticles(Context ctx) {
        Collection<Article> MyArticles = articleService.getAllArticles();
        ctx.json(MyArticles);
    }

    public static void getArticleById(Context ctx) {
        long id = Integer.parseInt(ctx.pathParam("id"));
        Article myArticle = articleService.getArticleById(id);
        ctx.json(myArticle);
    }

    public static void createArticle(Context ctx) {
        Article myArticle = ctx.bodyAsClass(Article.class);
        articleService.createArticle(myArticle);
        ctx.status(201);
    }

    public static void updateArticle(Context ctx) {
        Article myArticle = ctx.bodyAsClass(Article.class);
        String id = ctx.pathParam("id");
        Long articleId = Long.parseLong(id);
        myArticle.setArticleId(articleId);
        articleService.updateArticle(myArticle);
        ctx.status(200);
    }

    public static void deleteArticle(Context ctx) {
        String id = ctx.pathParam("id");
        Long articleId = Long.parseLong(id);
        articleService.deleteArticleById(articleId);
        ctx.status(200);
    }
}
