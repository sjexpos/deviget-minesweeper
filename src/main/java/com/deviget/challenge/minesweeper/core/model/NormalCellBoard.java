package com.deviget.challenge.minesweeper.core.model;

import java.util.Optional;

import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;

class NormalCellBoard implements GameCellBoard {

	private Game game;
	private int x;
	private int y;
	private CellBoard.State state;

	protected NormalCellBoard(Game game, int x, int y, CellBoard.State state) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.state = state;
	}

	public Optional<GameCellBoard> getTopLeftNeightbord() {
		return this.game.getCell(x-1, y-1);
	}
	
	public Optional<GameCellBoard> getTopMiddleNeightbord() {
		return this.game.getCell(x, y-1);
	}
	
	public Optional<GameCellBoard> getTopRightNeightbord() {
		return this.game.getCell(x+1, y-1);
	}
	
	public Optional<GameCellBoard> getRightMiddleNeightbord() {
		return this.game.getCell(x+1, y);
	}
	
	public Optional<GameCellBoard> getBottomRightNeightbord() {
		return this.game.getCell(x+1, y+1);
	}
	
	public Optional<GameCellBoard> getBottomMiddleNeightbord() {
		return this.game.getCell(x, y+1);
	}
	
	public Optional<GameCellBoard> getBottomLeftNeightbord() {
		return this.game.getCell(x-1, y+1);
	}
	
	public Optional<GameCellBoard> getLeftMiddleNeightbord() {
		return this.game.getCell(x-1, y);
	}

	/**
	 *      |   |
	 *    0 | 1 | 2
	 *   ------------
	 *    7 |   | 3
	 *   ------------
	 *    6 | 5 | 4
	 *      |   |
	 * 
	 * @return
	 */
	private Optional<GameCellBoard>[] createNeightbords() {
		Optional<GameCellBoard>[] neightbords = new Optional[8];
		neightbords[0] = getTopLeftNeightbord();
		neightbords[1] = getTopMiddleNeightbord();
		neightbords[2] = getTopRightNeightbord();
		neightbords[3] = getRightMiddleNeightbord();
		neightbords[4] = getBottomRightNeightbord();
		neightbords[5] = getBottomMiddleNeightbord();
		neightbords[6] = getBottomLeftNeightbord();
		neightbords[7] = getLeftMiddleNeightbord();
		return neightbords;
	}
	
	private int surroundMines(Optional<GameCellBoard>[] neightbords) {
		int mines = 0;
		for (int i=0; i < neightbords.length; i++) {
			if (neightbords[i].isPresent() && neightbords[i].get() instanceof MineCellBoard) {
				mines++;
			}
		}
		return mines;
	}

	public Game getGame() {
		return this.game;
	}
	
	@Override
	public int getX() {
		return this.x;
	}
	
	@Override
	public int getY() {
		return this.y;
	}
	
	@Override
	public State getState() {
		return this.state;
	}
	
	@Override
	public int surroundMines() {
		return surroundMines(createNeightbords());
	}

	@Override
	public void click() throws GameOverException {
		if (!CellBoard.State.COVERED.equals(getState())) {
			return;
		}
		Optional<GameCellBoard>[] neightbords = createNeightbords();
		setState(CellBoard.State.UNCOVERED);
		int surroundMines = surroundMines(neightbords);
		if (surroundMines == 0) {
			for (int i=0; i < neightbords.length; i++) {
				neightbords[i].orElse(new NoOpCellBoard()).click();
			}
		}
	}
	
	@Override
	public void flag() throws InvalidOperationException {
		if (!CellBoard.State.COVERED.equals(getState())) {
			throw new InvalidOperationException();
		}
		setState(CellBoard.State.FLAGGED);
	}
	
	@Override
	public void unflag() throws InvalidOperationException {
		if (!CellBoard.State.FLAGGED.equals(getState())) {
			throw new InvalidOperationException();
		}
		setState(CellBoard.State.COVERED);
	}
	
	private void setState(CellBoard.State state) {
		this.state = state;
		this.game.cellStateChanged(this);
	}
	
}
