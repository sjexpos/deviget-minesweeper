package com.deviget.challenge.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CellState {
	COVERED("C"),
	UNCOVERED("U"),
	FLAGGED("F"),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	MINE("*");
	
	private String value;
	
	CellState(String value) {
		this.value = value;
	}
	
	@JsonValue
	public String toValue() {
		return this.value;
	}
	
	static public CellState fromValue(String value) {
		for (CellState cs : values()) {
			if (cs.value.equals(value)) {
				return cs;
			}
		}
		return null;
	}
	
}
