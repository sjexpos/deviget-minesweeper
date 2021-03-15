package com.deviget.challenge.minesweeper.api.response;

import java.util.Date;

import com.deviget.challenge.minesweeper.core.model.Game;

public class GameResponse {
	private String id;
	private Date start;
	private Date end;
	private Game.State state;
	
	public GameResponse(Game game) {
		super();
		this.id = game.getId();
		this.start = game.getStart();
		this.end = game.getEnd();
		this.state = game.getState();
	}

	public String getId() {
		return id;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public Game.State getState() {
		return state;
	}

}
