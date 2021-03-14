package com.deviget.challenge.minesweeper.api.response;

import java.util.List;

public class MyGamesReponse {

	private List<GameResponse> games;

	public MyGamesReponse() {
		super();
	}

	public List<GameResponse> getGames() {
		return games;
	}

	public void setGames(List<GameResponse> games) {
		this.games = games;
	}
	
}
