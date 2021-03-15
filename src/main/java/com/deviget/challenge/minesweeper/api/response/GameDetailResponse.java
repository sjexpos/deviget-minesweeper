package com.deviget.challenge.minesweeper.api.response;

import java.util.Date;

import com.deviget.challenge.minesweeper.core.model.Game;

import io.swagger.annotations.ApiModelProperty;

public class GameDetailResponse extends GameOnPlayResponse {
	@ApiModelProperty(value="Date when the game was created")
	private Date start;
	@ApiModelProperty(value="Date when the game was finished. It's null when game state is STARTED and it has a value when game state is GAME_OVER or DONE")
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
