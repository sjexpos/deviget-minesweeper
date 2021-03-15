package com.deviget.challenge.minesweeper.core;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deviget.challenge.minesweeper.core.dao.GameRepository;
import com.deviget.challenge.minesweeper.core.exceptions.GameNotFoundException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;
import com.deviget.challenge.minesweeper.core.model.Game;
import com.deviget.challenge.minesweeper.core.model.User;

@Service
public class GameServiceImpl implements GameService {
	private final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

	@Autowired
	private GameRepository gameRepository;
	
	public GameServiceImpl() {
		super();
	}

	public List<Game> findByUser(User user) {
		LOGGER.debug("findByUser");
		return this.gameRepository.findByUser(user);
	}

	public Game createNewGame(User user, int columns, int rows, int mines) {
		Game game = Game.create(columns, rows, mines);
		game.setId("game::"+UUID.randomUUID().toString());
		game.setUser(user);
		this.gameRepository.save(game);
		return game;
	}

	public Game findUsersGame(User user, String gameId) throws GameNotFoundException {
		Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
		if (!user.equals(game.getUser())) {
			throw new GameNotFoundException(gameId);
		}
		return game;
	}

	public Game click(User user, String gameId, int column, int row) throws GameNotFoundException, InvalidOperationException {
		Game game = findUsersGame(user, gameId);
		game.click(column, row);
		this.gameRepository.save(game);
		return game;
	}
	
	public Game flag(User user, String gameId, int column, int row) throws GameNotFoundException, InvalidOperationException {
		Game game = findUsersGame(user, gameId);
		game.flag(column, row);
		this.gameRepository.save(game);
		return game;
	}
	
	public Game unflag(User user, String gameId, int column, int row) throws GameNotFoundException, InvalidOperationException {
		Game game = findUsersGame(user, gameId);
		game.unflag(column, row);
		this.gameRepository.save(game);
		return game;
	}
	
	
}
