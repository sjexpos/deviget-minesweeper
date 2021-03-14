package com.deviget.challenge.minesweeper.core.exceptions;

public class GameNotFoundException extends Exception {
	private static final long serialVersionUID = 1386118984859659543L;

	private String gameId;

	public GameNotFoundException(String gameId) {
		super();
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

}
