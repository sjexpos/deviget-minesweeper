package com.deviget.challenge.minesweeper.core.exceptions;

public class GameOverException extends Exception {
	private static final long serialVersionUID = -3610892005734956309L;

	public GameOverException() {
		super();
	}

	public GameOverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GameOverException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameOverException(String message) {
		super(message);
	}

	public GameOverException(Throwable cause) {
		super(cause);
	}

}
