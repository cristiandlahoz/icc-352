package org.example.models;

public class Tag {
	private static Long size = 0L;
	private Long tagId;
	private String name;

	public Tag( String name) {
		size++;
		this.tagId = size;
		this.name = name;
	}

	public Long getTagId() {
		return tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}


