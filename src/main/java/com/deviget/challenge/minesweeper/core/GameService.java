package com.deviget.challenge.minesweeper.core;

import java.util.List;

import com.deviget.challenge.minesweeper.core.exceptions.GameNotFoundException;
import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;
import com.deviget.challenge.minesweeper.core.model.Game;
import com.deviget.challenge.minesweeper.core.model.User;

public interface GameService {

	List<Game> findByUser(User user);
	
	Game createNewGame(User user, int columns, int rows, int mines);

	Game findUsersGame(User user, String gameId) throws GameNotFoundException;
	
	Game click(User user, String gameId, int column, int row) throws GameNotFoundException, InvalidOperationException;
	
	Game flag(User user, String gameId, int column, int row) throws GameNotFoundException, InvalidOperationException;
	
	Game unflag(User user, String gameId, int column, int row) throws GameNotFoundException, InvalidOperationException;
	
}
