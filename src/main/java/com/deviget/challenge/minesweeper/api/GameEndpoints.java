package com.deviget.challenge.minesweeper.api;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deviget.challenge.minesweeper.api.response.CreatedGameResponse;
import com.deviget.challenge.minesweeper.api.response.GameDetailResponse;
import com.deviget.challenge.minesweeper.api.response.GameOnPlayResponse;
import com.deviget.challenge.minesweeper.api.response.CellState;
import com.deviget.challenge.minesweeper.api.response.GameResponse;
import com.deviget.challenge.minesweeper.api.response.MyGamesReponse;
import com.deviget.challenge.minesweeper.core.GameService;
import com.deviget.challenge.minesweeper.core.exceptions.GameNotFoundException;

@RestController
@RequestMapping(Endpoints.GAME_ROOT_PATH)
public class GameEndpoints {
	private final Logger log = LoggerFactory.getLogger(GameEndpoints.class);

	private GameService service;
	
	@Autowired
	public GameEndpoints(GameService service) {
		super();
		this.service = service;
	}

	@GetMapping
	public MyGamesReponse getAllGame() {
		MyGamesReponse response = new MyGamesReponse();
		response.setGames(Arrays.asList(new GameResponse()));
		return response;
	}

	@GetMapping(value="/{gameId}")
	public GameDetailResponse getGame(@PathVariable(name="gameId")String gameId) throws GameNotFoundException {
//		GameDetailResponse response = new GameDetailResponse();
//		return response;
		throw new GameNotFoundException(gameId);
	}
	
	@PostMapping
	public CreatedGameResponse createGame(
			@RequestParam(name="columns", required=true) Integer columns,
			@RequestParam(name="rows", required=true) Integer rows,
			@RequestParam(name="mines", required=true) Integer mines) {
		return new CreatedGameResponse();
	}
	
	@PostMapping(value="/{gameId}/click")
	public GameOnPlayResponse clickOnGameBoard(
			@RequestParam(name="column", required=true) Integer column,
			@RequestParam(name="row", required=true) Integer row) throws GameNotFoundException {
		
		GameOnPlayResponse response = new GameOnPlayResponse();
		
		response.setGameState(new CellState[][] {
			{CellState.EMPTY, CellState.EMPTY, CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN},
			{CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN},
			{CellState.HIDDEN, CellState.HIDDEN, CellState.ONE, CellState.HIDDEN, CellState.HIDDEN},
			{CellState.FLAGGED, CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN},
			{CellState.HIDDEN, CellState.HIDDEN, CellState.HIDDEN, CellState.THREE, CellState.HIDDEN}
		});
		
		return response;
	}
	
	@PostMapping(value="/{gameId}/flag")
	public GameOnPlayResponse flagOnGameBoard(
			@RequestParam(name="column", required=true) Integer column,
			@RequestParam(name="row", required=true) Integer row) throws GameNotFoundException {
		
		return new GameOnPlayResponse();
	}
	
}
