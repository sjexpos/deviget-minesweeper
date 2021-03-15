package com.deviget.challenge.minesweeper.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deviget.challenge.minesweeper.api.response.CreatedGameResponse;
import com.deviget.challenge.minesweeper.api.response.GameDetailResponse;
import com.deviget.challenge.minesweeper.api.response.GameOnPlayResponse;
import com.deviget.challenge.minesweeper.api.response.GameResponse;
import com.deviget.challenge.minesweeper.api.response.MyGamesReponse;
import com.deviget.challenge.minesweeper.core.GameService;
import com.deviget.challenge.minesweeper.core.exceptions.GameNotFoundException;
import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;
import com.deviget.challenge.minesweeper.core.model.Game;
import com.deviget.challenge.minesweeper.core.model.User;

@RestController
@RequestMapping(Endpoints.GAME_ROOT_PATH)
@Validated
public class GameEndpoints extends AbstractEndpoints {
	private final Logger LOGGER = LoggerFactory.getLogger(GameEndpoints.class);

	@Autowired
	private GameService gameService;
	
	public GameEndpoints() {
		super();
	}

	@GetMapping
	public MyGamesReponse getAllMyGames() {
		LOGGER.info("getAllMyGames");
		User user = currentUser().get();
		List<Game> games = this.gameService.findByUser(user);
		List<GameResponse> myGames = games.stream()
				.map(g -> new GameResponse(g))
				.collect(Collectors.toList());
		MyGamesReponse response = new MyGamesReponse();
		response.setGames(myGames);
		return response;
	}

	@PostMapping
	public CreatedGameResponse createGame(
			@RequestParam(name="columns", required=true) @Min(value=4) @Max(value=100) Integer columns,
			@RequestParam(name="rows", required=true) @Min(value=4) @Max(value=100) Integer rows,
			@RequestParam(name="mines", required=true) @Min(value=4) @Max(value=100*100) Integer mines) {
		LOGGER.info("createGame");
		User user = currentUser().get();
		Game game = this.gameService.createNewGame(user, columns, rows, mines);
		return new CreatedGameResponse(game);
	}
	
	@GetMapping(value="/{gameId}")
	public GameDetailResponse getGame(@PathVariable(name="gameId") @NotBlank String gameId) throws GameNotFoundException {
		LOGGER.info("getGame");
		User user = currentUser().get();
		Game game = this.gameService.findUsersGame(user, gameId);
		GameDetailResponse response = new GameDetailResponse(game);
		return response;
	}
	
	@PostMapping(value="/{gameId}/click")
	public GameOnPlayResponse clickOnGameBoard(
			@PathVariable(name="gameId") @NotBlank String gameId,
			@RequestParam(name="column", required=true) @Min(value=0) @Max(value=100) Integer column,
			@RequestParam(name="row", required=true) @Min(value=0) @Max(value=100) Integer row) throws GameNotFoundException, InvalidOperationException, GameOverException {
		LOGGER.info("clickOnGameBoard");
		User user = currentUser().get();
		Game game = this.gameService.click(user, gameId, column, row);
		return new GameOnPlayResponse(game);
	}
	
	@PostMapping(value="/{gameId}/flag")
	public GameOnPlayResponse flagOnGameBoard(
			@PathVariable(name="gameId") @NotBlank String gameId,
			@RequestParam(name="column", required=true) @Min(value=0) @Max(value=100) Integer column,
			@RequestParam(name="row", required=true) @Min(value=0) @Max(value=100) Integer row) throws GameNotFoundException, InvalidOperationException {
		LOGGER.info("flagOnGameBoard");
		User user = currentUser().get();
		Game game = this.gameService.flag(user, gameId, column, row);
		return new GameOnPlayResponse(game);
	}
	
	@PostMapping(value="/{gameId}/unflag")
	public GameOnPlayResponse unflagOnGameBoard(
			@PathVariable(name="gameId") @NotBlank String gameId,
			@RequestParam(name="column", required=true) @Min(value=0) @Max(value=100) Integer column,
			@RequestParam(name="row", required=true) @Min(value=0) @Max(value=100) Integer row) throws GameNotFoundException, InvalidOperationException {
		LOGGER.info("unflagOnGameBoard");
		User user = currentUser().get();
		Game game = this.gameService.unflag(user, gameId, column, row);
		return new GameOnPlayResponse(game);
	}
	
}
