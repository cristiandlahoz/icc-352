package org.example.models;

import java.util.Date;
import java.util.ArrayList;

public class Article {
    private Long articleId;
    private String title;
    private String content;
    private User author;
    private Date date;
    private ArrayList<Comment> comments;
    private ArrayList<Tag> tags;

    public Article(Long articleId, String title, String content, User author, Date date) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
