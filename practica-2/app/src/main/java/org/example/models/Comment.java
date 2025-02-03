package org.example.models;


import java.sql.Date;

public class Comment {
	private Long commentId;
	private String comment;
	private User user;
	private Date date;
	private Article article;

	public Comment(Long commentId, String comment, User user, Date date, Article article) {
		this.commentId = commentId;
		this.comment = comment;
		this.user = user;
		this.date = date;
		this.article = article;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
}
