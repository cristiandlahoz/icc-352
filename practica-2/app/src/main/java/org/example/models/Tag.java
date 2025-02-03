package org.example.models;

public class Tag {
	private Long tagId;
	private String tagName;

	public Tag(Long tagId, String tagName) {
		this.tagId = tagId;
		this.tagName = tagName;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
