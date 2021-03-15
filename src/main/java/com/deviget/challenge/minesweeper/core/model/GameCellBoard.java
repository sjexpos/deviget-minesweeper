package com.deviget.challenge.minesweeper.core.model;

import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;

interface GameCellBoard extends CellBoard {

	void click() throws GameOverException;

	void flag() throws InvalidOperationException;
	
	void unflag() throws InvalidOperationException;


}
