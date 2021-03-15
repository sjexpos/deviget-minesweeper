package com.deviget.challenge.minesweeper.core.model;

import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;

class MineCellBoard extends NormalCellBoard {

	public MineCellBoard(Game game, int x, int y, CellBoard.State state) {
		super(game, x, y, state);
	}

	@Override
	public State getState() {
		if (Game.State.STARTED.equals(getGame().getState())) {
			return super.getState();
		}
		return State.MINE;
	}
	
	@Override
	public void click() throws GameOverException {
		throw new GameOverException("Boom!!");
	}
	
}
