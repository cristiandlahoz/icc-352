package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.example.models.Article;
import org.example.models.Comment;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.util.BaseController;

import java.util.*;

public class ArticleController extends BaseController {
    private static final ArticleService articleService = new ArticleService();

    public ArticleController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.get("/", ArticleController::getAllArticles);
        app.get("/articles/{id}", ArticleController::getArticleById);
        app.post("/articles", ArticleController::createArticle);
        app.post("/articles/{id}", ArticleController::updateArticle);
        app.delete("/articles/{id}", ArticleController::deleteArticle);

    };

    public static void getAllArticles(Context ctx) {
        List<Article> articleCollection = articleService.getAllArticles();
        Collection<Tag> tagCollection = TagController.getAllTags();
        Boolean logged = ctx.sessionAttribute("USUARIO") != null ? true : false;
        Map<String, Object> model = setModel(
                "title", "Wornux",
                "articleCollection", articleCollection,
                "tagCollection", tagCollection,
                "logged", logged);

        ctx.render("/public/index.html", model);
    }

    public static void getArticleById(Context ctx) {
        long articleId = Long.parseLong(ctx.pathParam("id"));
        Article myArticle = articleService.getArticleById(articleId);

        Collection<Tag> tags = myArticle.getTags();
        List<Article> authorArticles = articleService.getArticleByAuthor(myArticle.getAuthor());
        User author = ctx.sessionAttribute("USUARIO");
        String username = "";
        Boolean logged = false;
        if (author != null) {
            logged = true;
            username = author.getUsername();
        }
        List<Comment> comments = new CommentService().getCommentsByArticleId(articleId);
        Map<String, Object> model = setModel(
                "title", "Wornux",
                "article", myArticle,
                "tags", tags,
                "logged", logged,
                "authorArticles", authorArticles,
                "comments", comments,
                "author", username);

        ctx.render("/public/templates/article-view.html", model);
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
