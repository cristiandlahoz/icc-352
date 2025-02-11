package org.example.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.example.models.Article;
import org.example.models.Comment;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.services.TagService;
import org.example.util.BaseController;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ArticleController extends BaseController {
    private static final ArticleService articleService = new ArticleService();
    private static final TagService tagService = new TagService();

    public ArticleController(Javalin app) {
        super(app);
    }

    @Override
    public void applyRoutes() {
        app.get("/", ArticleController::getAllArticles);
        app.get("/articles/{id}", ArticleController::getArticleById);
        app.post("/articles", ArticleController::createArticle);
        app.post("/articles/{id}", ArticleController::updateArticle);
        app.post("/articles/form/{id}", ArticleController::formHandler);
        app.delete("/articles/{id}", ArticleController::deleteArticle);
    }

    public static void getAllArticles(Context ctx) {
        List<Article> articleCollection = articleService.getAllArticles();
        Collection<Tag> tagCollection = TagController.getAllTags();
        Boolean logged = ctx.sessionAttribute("USUARIO") != null ? true : false;
        User user = ctx.sessionAttribute("USUARIO");
        String role = (user != null) ? user.getRole().toString() : "GUEST";
        ctx.sessionAttribute("ROL", role);
        Map<String, Object> model = setModel(
                "title", "Wornux",
                "articleCollection", articleCollection,
                "tagCollection", tagCollection,
                "logged", logged,
                "role", role);

        ctx.render("/public/index.html", model);
    }

    public static void getArticleById(Context ctx) {
        long articleId = Long.parseLong(ctx.pathParam("id"));
        Article myArticle = articleService.getArticleById(articleId);
        if (myArticle == null)
            ctx.status(404).result("Article not found");

        Collection<Tag> tags = myArticle.getTags();
        List<Article> authorArticles = articleService.getArticleByAuthor(myArticle.getAuthor());
        User user = ctx.sessionAttribute("USUARIO");
        String username = "";
        Boolean logged = false;
        if (user != null) {
            logged = true;
            username = user.getUsername();
        }
        List<Comment> comments = new CommentService().getCommentsByArticleId(articleId);
        Map<String, Object> model = setModel(
                "title", "Wornux",
                "article", myArticle,
                "tags", tags,
                "logged", logged,
                "authorArticles", authorArticles,
                "comments", comments,
                "user", username);

        ctx.render("/public/templates/article-view.html", model);
    }

    public static void createArticle(Context ctx) {
        Article newArticle = article_process(ctx, null);

        if (newArticle != null) {
            ctx.status(201);
            ctx.redirect("/");
        } else {
            ctx.status(400).result("Error Creating Article");
        }
    }

    public static void formHandler(Context ctx) {
        Long articleId = Long.parseLong(ctx.pathParam("id"));
        String title = ctx.formParam("title");
        String content = ctx.formParam("content");
        String tags = ctx.formParam("tags");
        User author = ctx.sessionAttribute("USUARIO");
        if (author == null) {
            ctx.status(401).result("You are not logged");
            return;
        }

        List<String> selectedTags = new ArrayList<>();
        if (tags != null && !tags.trim().isEmpty()) {
            selectedTags = Arrays.asList(tags.split(","));
        }

        ArrayList<Tag> tagArrayList = new ArrayList<>();
        for (String tagName : selectedTags) {
            if (tagName != null && !tagName.trim().isEmpty()) {
                tagArrayList.add(tagService.createTag(tagName.trim()));
            }
        }

        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            ctx.status(404).result("Article not found");
            return;
        }
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tagArrayList);

        articleService.updateArticle(article);// Actualizar
        ctx.status(200).redirect("/articles/" + articleId);

        return;
    }

    public static void updateArticle(Context ctx) {
        try {
            Long articleId = Long.parseLong(ctx.pathParam("id"));

            Article existingArticle = articleService.getArticleById(articleId);
            if (existingArticle == null) {
                ctx.status(404).result("Article not found");
                return;
            }
            Map<String, Object> model = setModel("article", existingArticle);
            ctx.render("/public/templates/edit_article.html", model);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error Updating Article");
        }
    }

    public static Article article_process(Context ctx, Article article) {
        try {
            String title = ctx.formParam("title");
            String content = ctx.formParam("content");
            String tags = ctx.formParam("tags");
            User author = ctx.sessionAttribute("USUARIO");
            if (author == null) {
                ctx.status(401).result("You are not logged");
                return null;
            }

            List<String> selectedTags = new ArrayList<>();
            if (tags != null && !tags.trim().isEmpty()) {
                selectedTags = Arrays.asList(tags.split(","));
            }

            ArrayList<Tag> tagArrayList = new ArrayList<>();
            for (String tagName : selectedTags) {
                if (tagName != null && !tagName.trim().isEmpty()) {
                    tagArrayList.add(tagService.createTag(tagName.trim()));
                }
            }

            if (article == null) { // Creo
                Article newArticle = new Article(title, content, author.getUsername(), new Date());
                newArticle.setTags(tagArrayList);

                articleService.createArticle(newArticle);
                return newArticle;
            } else {
                article.setTitle(title);
                article.setContent(content);
                article.setTags(tagArrayList);

                articleService.updateArticle(article);

                getArticleById(ctx);

                return article;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteArticle(Context ctx) {
        try {
            Long articleId = Long.parseLong(ctx.pathParam("id"));

            Article existingArticle = articleService.getArticleById(articleId);
            if (existingArticle == null) {
                ctx.status(404).result("Article not found");
                return;
            }
            articleService.deleteArticleById(articleId);
            ctx.status(200).result("Article deleted");

        } catch (NumberFormatException e) {
            ctx.status(400).result("Article ID not valid");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error deleting article");
        }
    }

}
