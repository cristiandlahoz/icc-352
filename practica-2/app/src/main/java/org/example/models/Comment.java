package org.example.models;


import java.sql.Date;

public class Comment {
	private Long commentId;
	private String comment;
	private User user;
	private Date date;
	private Long articleId;

	public Comment(Long commentId, String comment, User user, Date date, Long articleId) {
		this.commentId = commentId;
		this.comment = comment;
		this.user = user;
		this.date = date;
		this.articleId = articleId;
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

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
}
