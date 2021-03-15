package com.deviget.challenge.minesweeper.api.response;

import com.deviget.challenge.minesweeper.core.model.CellBoard;
import com.deviget.challenge.minesweeper.core.model.Game;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameOnPlayResponse {
	@JsonProperty("id")
	private String gameId;
	@JsonProperty("state")
	private Game.State state;
	@JsonProperty("board")
	private CellState[][] board;

	public GameOnPlayResponse(Game game) {
		super();
		this.gameId = game.getId();
		this.state = game.getState();
		this.board = new CellState[game.getColumns()][game.getRows()];
		for (int x=0; x < game.getColumns(); x++) {
			for (int y=0; y < game.getRows(); y++) {
				CellState cellState = CellState.COVERED;
				CellBoard cell = game.getCellBoard(x, y);
				int surroundMines = cell.surroundMines();
				if (CellBoard.State.COVERED.equals(cell.getState())) {
					cellState = CellState.COVERED;
				} else if (CellBoard.State.FLAGGED.equals(cell.getState())) {
					cellState = CellState.FLAGGED;
				} else if (CellBoard.State.MINE.equals(cell.getState())) {
					cellState = CellState.MINE;
				} else {
					cellState = CellState.UNCOVERED;
					if (surroundMines > 0) {
						cellState = CellState.fromValue(Integer.toString(surroundMines));
					}
				}
				this.board[x][y] = cellState;
			}
		}
	}
	
}
