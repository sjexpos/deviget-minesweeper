package com.deviget.challenge.minesweeper.core.exceptions;

public class InvalidOperationException extends Exception {
	private static final long serialVersionUID = -3586076762168090421L;

	public InvalidOperationException() {
		super();
	}

	public InvalidOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidOperationException(String message) {
		super(message);
	}

	public InvalidOperationException(Throwable cause) {
		super(cause);
	}

}
