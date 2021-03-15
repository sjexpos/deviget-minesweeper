package com.deviget.challenge.minesweeper.api.response;

import com.deviget.challenge.minesweeper.core.model.CellBoard;
import com.deviget.challenge.minesweeper.core.model.Game;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class GameOnPlayResponse {
	@ApiModelProperty(value="Game identifier")
	private String id;
	@ApiModelProperty(value="Game state - STARTED: Game was created and it's ready to play / GAME_OVER: User clicks on some mines and game over / DONE: User discovered all mines and complete the game")
	private Game.State state;
	@ApiModelProperty(value="Board state - \n"
			+ "C: COVERED \n"
			+ "U: UNCOVERED \n"
			+ "F: FLAGGED \n"
			+ "1: there is one mines around the cell \n"
			+ "2: there are two mines around the cell \n"
			+ "3: there are three mines around the cell \n"
			+ "4: there are four mines around the cell \n"
			+ "5: there are five mines around the cell \n"
			+ "6: there are six mines around the cell \n"
			+ "7: there are seven mines around the cell \n"
			+ "8: there are eight mines around the cell \n"
			+ "*: cell contains a mines. It only appear when game state is GAME_OVER or DONE.")
	@JsonProperty("board")
	private CellState[][] board;

	public GameOnPlayResponse(Game game) {
		super();
		this.id = game.getId();
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

	public String getId() {
		return id;
	}

	public Game.State getState() {
		return state;
	}

	public CellState[][] getBoard() {
		return board;
	}
	
}
