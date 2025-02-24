package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.util.*;
import org.example.models.Article;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.services.TagService;
import org.example.util.BaseController;
import org.example.util.PageSize;
import org.example.util.Routes;
import org.example.util.SessionKeys;

public class ArticleController extends BaseController {
  private final ArticleService articleService;
  private final TagService tagService;
  private final CommentService commentService;

  public ArticleController(
      Javalin app,
      ArticleService articleService,
      TagService tagService,
      CommentService commentService) {
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
    final int page = parseQueryParam(ctx.queryParam("page"), 1);
    final int pageSize = parseQueryParam(ctx.queryParam("size"), PageSize.DEFAULT.getSize());
    final String tagName = ctx.queryParam("tag");

    final List<Article> articleCollection =
        Optional.ofNullable(tagName)
            .filter(tag -> !tag.isEmpty())
            .map(tag -> articleService.getAllArticlesByTag(page, pageSize, tag))
            .orElseGet(() -> articleService.getAllArticles(page, pageSize));

    final long countPages =
        Optional.ofNullable(tagName)
            .filter(tag -> !tag.isEmpty())
            .map(articleService::countAllArticlesByTag)
            .orElseGet(articleService::countAllArticles);

    final Collection<Tag> tagCollection = tagService.getAllTags();

    final User user = ctx.sessionAttribute(SessionKeys.USER.getKey());
    final boolean logged = Optional.ofNullable(user).isPresent();
    final String role =
        Optional.ofNullable(user).map(User::getRole).map(Enum::toString).orElse("GUEST");

    final Map<String, Object> model =
        setModel(
            "title",
            "Wornux",
            "user",
            user,
            "articleCollection",
            articleCollection,
            "tagCollection",
            tagCollection,
            "logged",
            logged,
            "role",
            role,
            "currentPage",
            page,
            "tag",
            tagName,
            "countPages",
            (int) Math.ceil((double) countPages / pageSize));

    ctx.render("index.html", model);
  }

  private int parseQueryParam(String param, int defaultValue) {
    return Optional.ofNullable(param)
        .filter(p -> p.matches("\\d+"))
        .map(Integer::parseInt)
        .orElse(defaultValue);
  }

  public void getArticleById(Context ctx) {
    var articleId =
        parseArticleId(ctx.pathParam("id"))
            .orElseThrow(() -> new BadRequestResponse("Invalid article ID"));

    var myArticle =
        Optional.ofNullable(articleService.getArticleById(articleId))
            .orElseThrow(() -> new NotFoundResponse("Article not found"));

    var tags = Optional.ofNullable(myArticle.getTags()).orElseGet(List::of);

    var authorArticles =
        Optional.ofNullable(myArticle.getAuthor())
            .map(author -> articleService.getArticleByAuthor(author.getName()))
            .orElseGet(List::of);

    Optional<User> user = Optional.ofNullable(ctx.sessionAttribute(SessionKeys.USER.getKey()));
    var role = user.map(u -> u.getRole().toString()).orElse("GUEST");
    var myUser = user.orElse(myArticle.getAuthor());
    var logged = user.isPresent();

    var comments =
        Optional.ofNullable(commentService.getCommentsByArticleId(articleId)).orElseGet(List::of);

    var model =
        Map.of(
            "title", "Wornux",
            "article", myArticle,
            "tags", tags,
            "logged", logged,
            "role", role,
            "authorArticles", authorArticles,
            "comments", comments,
            "user", myUser);

    ctx.render("/pages/article-view.html", model);
  }

  private Optional<Long> parseArticleId(String id) {
    try {
      return Optional.of(Long.parseLong(id));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  public void createArticle(Context ctx) {
    Article newArticle = article_process(ctx, null);

    if (newArticle != null) {
      ctx.status(201);
      ctx.redirect(Routes.ARTICLES.getPath());
    } else {
      ctx.status(400).result("Error Creating Article");
      // revisar
      ctx.redirect(Routes.CREATEARTICLE.getPath());
    }
  }

  public void formHandler(Context ctx) {
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

    articleService
        .updateArticle(article)
        .ifPresentOrElse(
            a -> ctx.status(200).redirect(Routes.ARTICLES.getPath() + "/" + a.getArticleId()),
            () -> updateArticle(ctx));
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
      ctx.status(500).result("Error Updating Article" + e.getMessage());
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
      if (tags != null && !tags.trim().isBlank()) {
        selectedTags = Arrays.asList(tags.split(","));
      }

      ArrayList<Tag> tagArrayList = new ArrayList<>();
      for (String tagName : selectedTags) {
        if (tagName != null && !tagName.trim().isEmpty()) {
          tagArrayList.add(tagService.createTag(tagName.trim()));
        }
      }

      if (article == null) {
        Article newArticle = articleService.createArticle(title, content, author.getUsername());
        System.out.println(author.getUsername());
        newArticle.setTags(tagArrayList);

        return newArticle;
      } else {
        article.setTitle(title);
        article.setContent(content);
        articleService.updateArticle(article);

        getArticleById(ctx);

        return article;
      }

    } catch (Exception e) {
      System.out.println("Error Updating Article" + e.getMessage());
      return null;
    }
  }

  public void deleteArticle(Context ctx) {
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
