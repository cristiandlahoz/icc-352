package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Article;
import org.example.services.ArticleService;

import java.util.*;

public class ArticleController {
	private static final ArticleService articleService = new ArticleService();

	public static void getAllArticles(Context ctx) {
		Collection<Article> articles = articleService.getAllArticles();
		ctx.json(articles);
	}

	public static void createArticle(Context ctx) {
		Article article = ctx.bodyAsClass(Article.class);
		articleService.createArticle(article);
		ctx.status(201);
	}

//	public static void deleteArticle(Context ctx) {
//		String idParam = ctx.pathParam("id");
//
//		try {
//			Long id = Long.parseLong(idParam);
//
//			if (articles.containsKey(id)) {
//				articles.remove(id);
//				ctx.status(200).result("Artículo con ID " + id + " eliminado correctamente.");
//			} else {
//				ctx.status(404).result("Artículo con ID " + id + " no encontrado.");
//			}
//		} catch (NumberFormatException e) {
//			ctx.status(400).result("ID inválido.");
//		}
//	}

}
