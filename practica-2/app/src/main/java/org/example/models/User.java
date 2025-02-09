package org.example.models;

import org.example.util.AccessStatus;
import org.example.util.Role;

public class User {
	private String username;
	private String name;
	private String password;
	private Role role;
	private AccessStatus accessStatus;

	public User(String username, String name, String password, Role role, AccessStatus accessStatus) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.role = role;
		this.accessStatus = AccessStatus.UNAUTHENTICATED;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public AccessStatus isAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(AccessStatus accessStatus) {
		this.accessStatus = accessStatus;
	}
}
