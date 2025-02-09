package org.example.models;


import java.util.*;

public class Comment {
	private Long commentId;
	private String comment;
	private String username;
	private Date date;
	private Long articleId;

	public Comment(Long commentId, String comment, String username, Long articleId) {
		this.commentId = commentId;
		this.comment = comment;
		this.username = username;
		this.date = new Date();
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
