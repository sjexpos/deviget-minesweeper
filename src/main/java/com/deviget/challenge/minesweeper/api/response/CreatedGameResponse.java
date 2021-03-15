package com.deviget.challenge.minesweeper.api.response;

import java.util.Date;

import com.deviget.challenge.minesweeper.core.model.Game;

public class CreatedGameResponse {
	private String id;
	private Date start;
	private Game.State state;

	public CreatedGameResponse(Game game) {
		super();
		this.id = game.getId();
		this.start = game.getStart();
		this.state = game.getState();
	}

	public String getId() {
		return id;
	}

	public Date getStart() {
		return start;
	}

	public Game.State getState() {
		return state;
	}

}
