package com.deviget.challenge.minesweeper.api.response;

import java.util.Date;

import com.deviget.challenge.minesweeper.core.model.Game;

public class GameDetailResponse extends GameOnPlayResponse {
	private Date start;
	private Date end;

	public GameDetailResponse(Game game) {
		super(game);
		this.start = game.getStart();
		this.end = game.getEnd();
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

}
