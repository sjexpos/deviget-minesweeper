package com.deviget.challenge.minesweeper.api.response;

public class SessionInfoRespose {
	private String token;

	public SessionInfoRespose(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
