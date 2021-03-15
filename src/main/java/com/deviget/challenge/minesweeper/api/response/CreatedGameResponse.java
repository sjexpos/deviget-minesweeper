package com.deviget.challenge.minesweeper.api.response;

import java.util.Date;

import com.deviget.challenge.minesweeper.core.model.Game;

import io.swagger.annotations.ApiModelProperty;

public class CreatedGameResponse {
	@ApiModelProperty(value="Identifier of the created game")
	private String id;
	@ApiModelProperty(value="Date when the game was created")
	private Date start;
	@ApiModelProperty(value="Game state - STARTED: Game was created and it's ready to play / GAME_OVER: User clicks on some mines and game over / DONE: User discovered all mines and complete the game")
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
