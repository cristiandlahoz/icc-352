package org.example.models;

import java.sql.Date;
import java.util.ArrayList;

public class Article {
	private Long articleId;
	private String articleTitle;
	private String articleContent;
	private User articleAuthor;
	private Date articleDate;
	private ArrayList<Comment> articleComments;
	private ArrayList<Tag> articleTags;

	public Article(Long articleId, String articleContent, String articleTitle, User articleAuthor, Date articleDate, ArrayList<Comment> articleComments, ArrayList<Tag> articleTags) {
		this.articleId = articleId;
		this.articleContent = articleContent;
		this.articleTitle = articleTitle;
		this.articleAuthor = articleAuthor;
		this.articleDate = articleDate;
		this.articleComments = articleComments;
		this.articleTags = articleTags;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}

	public User getArticleAuthor() {
		return articleAuthor;
	}

	public void setArticleAuthor(User articleAuthor) {
		this.articleAuthor = articleAuthor;
	}

	public Date getArticleDate() {
		return articleDate;
	}

	public void setArticleDate(Date articleDate) {
		this.articleDate = articleDate;
	}

	public ArrayList<Comment> getArticleComments() {
		return articleComments;
	}

	public void setArticleComments(ArrayList<Comment> articleComments) {
		this.articleComments = articleComments;
	}

	public ArrayList<Tag> getArticleTags() {
		return articleTags;
	}

	public void setArticleTags(ArrayList<Tag> articleTags) {
		this.articleTags = articleTags;
	}
}
