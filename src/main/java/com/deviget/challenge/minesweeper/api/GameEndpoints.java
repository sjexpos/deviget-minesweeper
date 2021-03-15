package com.deviget.challenge.minesweeper.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

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

	@ApiOperation(value="Get games that signed user can see", authorizations = { @Authorization(value = "authorization_key") })
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Request Successful")
	})
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

	@ApiOperation(value="Create and start a new games associated to the requestering user", authorizations = { @Authorization(value = "authorization_key") })
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="columns", value="How many columns do you want in the new game?"),
			@ApiImplicitParam(name="rows", value="How many rows do you want in the new game?"),
			@ApiImplicitParam(name="mines", value="How many mines do you want in the new game? (mines must be less than cells)")
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Request Successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
	@PostMapping
	public CreatedGameResponse createGame(
			@RequestParam(name="columns", required=true) @Min(value=4) @Max(value=100) Integer columns,
			@RequestParam(name="rows", required=true) @Min(value=4) @Max(value=100) Integer rows,
			@RequestParam(name="mines", required=true) @Min(value=4) @Max(value=100*100) Integer mines) {
		LOGGER.info("createGame");
		if (columns*rows <= mines) {
			throw new ConstraintViolationException("{minesweeper.validation.too.many.mines}", null);
		}
		User user = currentUser().get();
		Game game = this.gameService.createNewGame(user, columns, rows, mines);
		return new CreatedGameResponse(game);
	}
	
	@ApiOperation(value="Retrieve information and board state of a game", authorizations = { @Authorization(value = "authorization_key") })
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="gameId", value="Game identifier")
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Request Successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=404, message="If game does not exist or requestering user can not see that game", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
	@GetMapping(value="/{gameId}")
	public GameDetailResponse getGame(@PathVariable(name="gameId") @NotBlank String gameId) throws GameNotFoundException {
		LOGGER.info("getGame");
		User user = currentUser().get();
		Game game = this.gameService.findUsersGame(user, gameId);
		GameDetailResponse response = new GameDetailResponse(game);
		return response;
	}
	
	@ApiOperation(value="The user left-clicks a cell to uncover it", authorizations = { @Authorization(value = "authorization_key") })
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="gameId", value="Game identifier"),
			@ApiImplicitParam(name="column", value="Which column?"),
			@ApiImplicitParam(name="row", value="Which row?"),
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Request Successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=404, message="If game does not exist or requestering user can not see that game", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=409, message="If the game is over or completed", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
	@PostMapping(value="/{gameId}/click")
	public GameOnPlayResponse clickOnGameBoard(
			@PathVariable(name="gameId") @NotBlank String gameId,
			@RequestParam(name="column", required=true) @Min(value=0) @Max(value=100) Integer column,
			@RequestParam(name="row", required=true) @Min(value=0) @Max(value=100) Integer row) throws GameNotFoundException, InvalidOperationException {
		LOGGER.info("clickOnGameBoard");
		User user = currentUser().get();
		Game game = this.gameService.click(user, gameId, column, row);
		return new GameOnPlayResponse(game);
	}
	
	@ApiOperation(value="The user right-clicks a cell to flag it", authorizations = { @Authorization(value = "authorization_key") })
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="gameId", value="Game identifier"),
			@ApiImplicitParam(name="column", value="Which column?"),
			@ApiImplicitParam(name="row", value="Which row?"),
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Request Successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=404, message="If game does not exist or requestering user can not see that game", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=409, message="If the game is over or completed. If the cell was already flagged", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
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
	
	@ApiOperation(value="The user right-clicks a cell to unflag it", authorizations = { @Authorization(value = "authorization_key") })
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="gameId", value="Game identifier"),
			@ApiImplicitParam(name="column", value="Which column?"),
			@ApiImplicitParam(name="row", value="Which row?"),
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Request Successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=404, message="If game does not exist or requestering user can not see that game", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=409, message="If the game is over or completed. If the cell was not flagged before", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
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
