package org.example.controllers;

import java.util.*;

import org.example.models.Article;
import org.example.models.Comment;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.services.TagService;
import org.example.util.BaseController;
import org.example.util.Routes;
import org.example.util.SessionKeys;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ArticleController extends BaseController {
    private final ArticleService articleService;
    private final TagService tagService;
    private final CommentService commentService;

    public ArticleController(Javalin app, ArticleService articleService, TagService tagService, CommentService commentService) {
        super(app);
        this.articleService = articleService;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    @Override
    public void applyRoutes() {
        app.get(Routes.CREATEARTICLE.getPath(), this::renderCreateArticlePage);
        app.get(Routes.ARTICLES.getPath(), this::getAllArticles);
        app.get(Routes.ARTICLE.getPath(), this::getArticleById);
        app.post(Routes.ARTICLES.getPath(), this::createArticle);
        app.post(Routes.ARTICLE.getPath(), this::updateArticle);
        app.post("/articles/form/{id}", this::formHandler);
        app.delete(Routes.ARTICLE.getPath(), this::deleteArticle);
    }

    private void renderCreateArticlePage(Context ctx) {
        ctx.render("/pages/create_article.html");
    }

    public void getAllArticles(Context ctx) {
        List<Article> articleCollection = articleService.getAllArticles();
        Collection<Tag> tagCollection = tagService.getAllTags();
        Boolean logged = ctx.sessionAttribute(SessionKeys.USER.getKey()) != null;
        User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
        String role = (user != null) ? user.getRole().toString() : "GUEST";
        ctx.sessionAttribute("ROL", role);
        Map<String, Object> model = setModel(
                "title", "Wornux",
                "articleCollection", articleCollection,
                "tagCollection", tagCollection,
                "logged", logged,
                "role", role);

        ctx.render("index.html", model);
    }

    public void getArticleById(Context ctx) {
        long articleId = Long.parseLong(ctx.pathParam("id"));
        Article myArticle = articleService.getArticleById(articleId);
        if (myArticle == null)
            ctx.status(404).result("Article not found");

	    assert myArticle != null;
	    Collection<Tag> tags = myArticle.getTags();
        List<Article> authorArticles = articleService.getArticleByAuthor(myArticle.getAuthor());
        User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
        String role = (user != null) ? user.getRole().toString() : "GUEST";
        String username = "";
        Boolean logged = false;
        if (user != null) {
            logged = true;
            username = user.getUsername();
        }
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        Map<String, Object> model = setModel(
                "title", "Wornux",
                "article", myArticle,
                "tags", tags,
                "logged", logged,
                "role", role,
                "authorArticles", authorArticles,
                "comments", comments,
                "user", username);

        ctx.render("/pages/article-view.html", model);
    }

    public  void createArticle(Context ctx) {
        Article newArticle = article_process(ctx, null);

        if (newArticle != null) {
            ctx.status(201);
            ctx.redirect("/");
        } else {
            ctx.status(400).result("Error Creating Article");
        }
    }

    public  void formHandler(Context ctx) {
        Long articleId = Long.parseLong(ctx.pathParam("id"));
        String title = ctx.formParam("title");
        String content = ctx.formParam("content");
        String tags = ctx.formParam("tags");
        User author = ctx.sessionAttribute(SessionKeys.USER.getKey());
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

        articleService.updateArticle(article);
        ctx.status(200).redirect(Routes.ARTICLES.getPath() + "/" + articleId);
        return;
    }

    public void updateArticle(Context ctx) {
        try {
            Long articleId = Long.parseLong(ctx.pathParam("id"));

            Article existingArticle = articleService.getArticleById(articleId);
            if (existingArticle == null) {
                ctx.status(404).result("Article not found");
                return;
            }
            Map<String, Object> model = setModel("article", existingArticle);
            ctx.render("/pages/edit_article.html", model);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error Updating Article");
        }
    }

    public Article article_process(Context ctx, Article article) {
        try {
            String title = ctx.formParam("title");
            String content = ctx.formParam("content");
            String tags = ctx.formParam("tags");
            User author = ctx.sessionAttribute(SessionKeys.USER.getKey());
            if (author == null) {
                ctx.status(401).result("You are not logged");
                return null;
            }

            List<String> selectedTags = new ArrayList<>();
            if (tags != null && !tags.trim().isEmpty()) {
                selectedTags = Arrays.asList(tags.split(","));
            }

            ArrayList<Tag> tagArrayList = new ArrayList<>() ;
            for (String tagName : selectedTags) {
                if (tagName != null && !tagName.trim().isEmpty()) {
                    tagArrayList.add(tagService.createTag(tagName.trim()));
                }
            }

            if (article == null) {
                Article newArticle = articleService.createArticle(title, content, author.getUsername());
                newArticle.setTags(tagArrayList);

                return newArticle;
            } else {
                article.setTitle(title);
                article.setContent(content);
                //article.setTags(tagArrayList);

                articleService.updateArticle(article);

                getArticleById(ctx);

                return article;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public  void deleteArticle(Context ctx) {
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
            ctx.status(500).result("Error deleting article");
        }
    }

}
