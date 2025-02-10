package org.example.services;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.models.Article;

public class ArticleService {

    private static final Map<Long, Article> articles = new HashMap<>();

    public ArticleService() {

    }

    static {
        articles.put(1L, new Article("Introducción a la Programación en Java",
                "Java es un lenguaje de programación ampliamente utilizado para el desarrollo de aplicaciones empresariales y móviles. Su sintaxis clara y su fuerte tipado lo hacen ideal para proyectos de gran escala.",
                "Theprimeagen", new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000))); // Hace 10 días

        articles.put(2L, new Article("Guía Básica de Bases de Datos SQL",
                "Las bases de datos SQL permiten almacenar y gestionar datos de manera estructurada. A través de consultas SQL, es posible recuperar información de manera eficiente y segura.",
                "", new Date(System.currentTimeMillis() - 20L * 24 * 60 * 60 * 1000))); // Hace 20 días

        articles.put(3L, new Article("Cómo Crear una API con Spring Boot",
                "Spring Boot facilita la creación de APIs RESTful en Java, proporcionando una configuración automática y herramientas que simplifican el desarrollo.",
                "user1", new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000))); // Hace 5 días

        articles.put(4L, new Article("Optimización de Código en JavaScript",
                "JavaScript es un lenguaje esencial para el desarrollo web. Aprender a optimizar su código mejora el rendimiento y la experiencia del usuario en aplicaciones web.",
                "user2", new Date(System.currentTimeMillis() - 15L * 24 * 60 * 60 * 1000))); // Hace 15 días

        articles.put(5L, new Article("Seguridad en Aplicaciones Web",
                "La seguridad en aplicaciones web es crucial para proteger los datos del usuario. Implementar buenas prácticas como validación de entradas y protección contra ataques XSS y CSRF es fundamental.",
                "user1", new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000))); // Hace 7 días

        articles.put(6L, new Article("Principios de Diseño de Software",
                "El diseño de software debe seguir principios como SOLID para mejorar la mantenibilidad y escalabilidad de los sistemas.",
                "user2", new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000))); // Hace 30 días

        articles.put(7L, new Article(
                "Docker para Desarrolladores",
                "Docker es una plataforma que permite a los desarrolladores crear, desplegar y ejecutar aplicaciones en contenedores.\n Un contenedor encapsula una aplicación junto con todas sus dependencias, garantizando que funcione de la misma manera en cualquier entorno, ya sea en desarrollo, pruebas o producción.\n\n Uno de los principales beneficios de Docker es la portabilidad. Al usar imágenes ligeras y reproducibles, los desarrolladores pueden asegurarse de que el software funcione sin problemas en distintos sistemas operativos y configuraciones. Además, Docker optimiza el uso de recursos al compartir el kernel del sistema operativo entre múltiples contenedores, lo que lo hace más eficiente en comparación con las máquinas virtuales tradicionales.\n\n Entre las herramientas clave de Docker se encuentran Docker Compose, que permite definir y administrar aplicaciones multicontenedor, y Docker Hub, un repositorio donde se pueden encontrar y compartir imágenes preconfiguradas.\n\n En conclusión, Docker se ha convertido en una tecnología esencial para los desarrolladores modernos, facilitando el despliegue y la escalabilidad de aplicaciones en la nube y entornos locales.",
                "Theprimeagen", new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000))); // Hace 3 días

        articles.put(8L, new Article("Machine Learning con Python",
                "El aprendizaje automático está revolucionando diversas industrias. Python, con bibliotecas como scikit-learn y TensorFlow, permite crear modelos predictivos de manera eficiente.",
                "user2", new Date(System.currentTimeMillis() - 1L * 24 * 60 * 60 * 1000))); // Hace 1 día
    }

    public List<Article> getAllArticles() {
        return articles.values().stream().sorted(Comparator.comparing(Article::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Article getArticleById(Long articleId) {
        if (articleId == null) {
            throw new IllegalArgumentException("tagId cannot be null");
        } else
            return articles.get(articleId);
    }

    public List<Article> getArticleByAuthor(String author) {
        return articles.values().stream().filter(article -> article.getAuthor().equals(author))
                .sorted(Comparator.comparing(Article::getDate).reversed()).collect(Collectors.toList());
    }

    public void createArticle(Article article) {
        articles.put(article.getArticleId(), article);
    }

    public void updateArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article cannot be null");
        } else if (!articles.containsKey(article.getArticleId())) {
            throw new IllegalArgumentException("Article not found");
        } else {
            Article myArticle = articles.get(article.getArticleId());
            myArticle.setTitle(article.getTitle());
            myArticle.setContent(article.getContent());
            myArticle.setTags(article.getTags());
            articles.put(myArticle.getArticleId(), myArticle);
        }
    }

    public void deleteArticleById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        } else if (!articles.containsKey(id)) {
            throw new IllegalArgumentException("Article not found");
        } else {
            articles.remove(id);
        }
    }

}
