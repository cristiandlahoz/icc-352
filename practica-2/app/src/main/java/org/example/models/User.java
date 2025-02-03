package org.example.models;

public class User {
	private String username;
	private String name;
	private String password;
	private boolean admin;
	private boolean autor;

	public User(String username, String name, String password, boolean admin, boolean autor) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.admin = admin;
		this.autor = autor;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAutor() {
		return autor;
	}

	public void setAutor(boolean autor) {
		this.autor = autor;
	}
}
