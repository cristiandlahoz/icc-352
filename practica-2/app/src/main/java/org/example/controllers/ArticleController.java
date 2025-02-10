package org.example.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.example.models.Article;
import org.example.models.Comment;
import org.example.models.Tag;
import org.example.models.User;
import org.example.services.ArticleService;
import org.example.services.CommentService;
import org.example.services.TagService;
import org.example.util.BaseController;

import java.util.*;
import java.util.stream.Collectors;

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
        Article article = article_process(ctx); // Recibe el artículo procesado
        if (article != null) {
            ctx.status(201);
            ctx.redirect("/");
        } else {
            ctx.status(400).result("Error creating article");
        }
    }

    public static Article article_process(Context ctx) {
        try {
            // Obtener datos del formulario
            String title = ctx.formParam("title");
            String content = ctx.formParam("content");
            String tags = ctx.formParam("tags");

            String author = ctx.sessionAttribute("USERNAME");

            // Manejar etiquetas
            List<String> selectedTags = new ArrayList<>();
            if (tags != null && !tags.trim().isEmpty()) {
                selectedTags = Arrays.asList(tags.split(","));
            }

            // Crear la lista de etiquetas
            ArrayList<Tag> tagArrayList = new ArrayList<>();
            for (String tagName : selectedTags) {
                if (tagName != null && !tagName.trim().isEmpty()) {
                    tagArrayList.add(tagService.createTag(tagName.trim()));
                }
            }

            //Article myArticle = ctx.bodyAsClass(Article.class);

            //Article myArticle = new Article(title, content, author, new Date());
            //tuve que poner esta así porque lo estoy probando fuera de sesion y me daba error
            Article myArticle = new Article(title, content, "Theprimeagen", new Date());
            myArticle.setTags(tagArrayList);

            // 🔹 Verificar los valores antes de guardar
            System.out.println("🔍 Verificando valores del artículo:");
            System.out.println("Título: " + myArticle.getTitle());
            System.out.println("Contenido: " + myArticle.getContent());
            System.out.println("Autor: " + myArticle.getAuthor());
            System.out.println("Fecha: " + myArticle.getDate());
            System.out.println("Tags:");
            for (Tag tag : myArticle.getTags()) {
                System.out.println(" - ID: " + tag.getTagId() + ", Nombre: " + tag.getName());
            }


            // Guardar artículo en la base de datos
            articleService.createArticle(myArticle);

            return myArticle; // Devolver el artículo creado
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
