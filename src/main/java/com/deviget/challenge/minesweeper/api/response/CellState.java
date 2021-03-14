package com.deviget.challenge.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CellState {
	HIDDEN("H"),
	EMPTY("E"),
	FLAGGED("F"),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8");
	
	private String value;
	
	CellState(String value) {
		this.value = value;
	}
	
	@JsonValue
	public String toValue() {
		return this.value;
	}
	
}
