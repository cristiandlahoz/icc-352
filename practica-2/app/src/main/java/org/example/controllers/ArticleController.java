package org.example.controllers;

import io.javalin.http.Context;
import org.example.models.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleController {
	private static List<Article> articles = new ArrayList<>();

	public static void getAllArticles(Context ctx) {
		ctx.json(articles);  // Devuelve JSON con los artículos
	}

	public static void createArticle(Context ctx) {
		Article article = ctx.bodyAsClass(Article.class);
		articles.add(article);
		ctx.status(201);
	}
}
