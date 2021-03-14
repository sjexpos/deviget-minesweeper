package com.deviget.challenge.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameOnPlayResponse {
	@JsonProperty("id")
	private String gameId;
	@JsonProperty("state")
	private CellState[][] gameState;

	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public CellState[][] getGameState() {
		return gameState;
	}
	public void setGameState(CellState[][] gameState) {
		this.gameState = gameState;
	}
	
}
