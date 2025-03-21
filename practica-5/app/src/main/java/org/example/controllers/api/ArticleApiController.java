package org.example.controllers.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.dto.ArticleDTO;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.util.BaseController;
import org.example.util.PageSize;
import org.example.util.Routes;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ArticleApiController extends BaseController {
  private final ArticleService articleService;

  public ArticleApiController(
      final Javalin app,
      final ArticleService articleService,
      final CommentService commentService) {
    super(app);
    this.articleService = articleService;
  }

  @Override
  public void applyRoutes() {
    app.get(Routes.ARTICLES_API.getPath(), this::getAllArticles);
  }

  public void getAllArticles(final Context ctx) {
    final int page = parseQueryParam(ctx.queryParam("page"), 1);
    final int pageSize = parseQueryParam(ctx.queryParam("size"), PageSize.DEFAULT.getSize());
    final String tagName = ctx.queryParam("tag");

    final List<ArticleDTO> articleCollection = Optional.ofNullable(tagName)
        .filter(tag -> !tag.isEmpty())
        .map(tag -> {
          return articleService.getAllArticlesByTag(page, pageSize, tag).stream().map(article -> {
            final String title = article.getTitle();
            final String content = article.getContent();
            final User author = article.getAuthor();
            final List<Tag> tags = article.getTags();
            final Date date = article.getDate();
            return new ArticleDTO(title, content, author.getUsername(), tags, date);
          }).toList();
        })
        .orElseGet(() -> articleService.getAllArticles(page, pageSize).stream().map(article -> {
          final String title = article.getTitle();
          final String content = article.getContent();
          final User author = article.getAuthor();
          final List<Tag> tags = article.getTags();
          final Date date = article.getDate();
          return new ArticleDTO(title, content, author.getUsername(), tags, date);
        }).toList());

    final long countPages = Optional.ofNullable(tagName)
        .filter(tag -> !tag.isEmpty())
        .map(articleService::countAllArticlesByTag)
        .orElseGet(articleService::countAllArticles);
    final Map<String, Object> response = new HashMap<>() {
      {
        put("articles", articleCollection);
        put("totalPages", (int) Math.ceil((double) countPages / pageSize));
      }
    };

    ctx.json(response);
  }

  private int parseQueryParam(final String param, final int defaultValue) {
    return Optional.ofNullable(param)
        .filter(p -> p.matches("\\d+"))
        .map(Integer::parseInt)
        .orElse(defaultValue);
  }
}
