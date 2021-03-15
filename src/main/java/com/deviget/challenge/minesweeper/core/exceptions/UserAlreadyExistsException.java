package com.deviget.challenge.minesweeper.core.exceptions;

public class UserAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 4184366722508463074L;

	private String username;

	public UserAlreadyExistsException(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
