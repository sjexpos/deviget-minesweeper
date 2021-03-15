package com.deviget.challenge.minesweeper.security;

import java.io.Serializable;

public class UserSession implements Serializable {
	private static final long serialVersionUID = -7629230600269415742L;

	private String token;
	private String userId;
	private String username;

	public UserSession() {
		super();
	}

	public UserSession(String token, String userId, String username) {
		super();
		this.token = token;
		this.userId = userId;
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
