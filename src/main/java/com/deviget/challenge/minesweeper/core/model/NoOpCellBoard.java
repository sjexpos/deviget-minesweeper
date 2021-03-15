package com.deviget.challenge.minesweeper.core.model;

import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;

class NoOpCellBoard implements GameCellBoard {

	@Override
	public int getX() {
		return -1;
	}
	
	@Override
	public int getY() {
		return -1;
	}
	
	@Override
	public State getState() {
		return State.COVERED;
	}
	
	@Override
	public int surroundMines() {
		return 0;
	}

	@Override
	public void click() throws GameOverException {
	}

	@Override
	public void flag() throws InvalidOperationException {
	}

	@Override
	public void unflag() throws InvalidOperationException {
	}

}
