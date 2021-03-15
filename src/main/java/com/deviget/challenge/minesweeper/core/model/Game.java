package com.deviget.challenge.minesweeper.core.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import com.deviget.challenge.minesweeper.core.exceptions.GameOverException;
import com.deviget.challenge.minesweeper.core.exceptions.InvalidOperationException;

import io.netty.util.internal.ThreadLocalRandom;

@Document
public class Game {
	static public enum State {
		STARTED,
		GAME_OVER,
		DONE
	}
	
	@Id
	private String id;
	private Date start;
	private Date end;
	private State state;
	private byte[][] boardState;
	private Set<Pair<Integer,Integer>> mines; 
	@DBRef
	private User user;
	@Transient
	private Map<Pair<Integer,Integer>, GameCellBoard> cachedCellsBoards = new HashMap<Pair<Integer,Integer>, GameCellBoard>();

	static public Game create(int columns, int rows, int mines) {
		Game game = new Game();
		game.start = new Date();
		game.end = null;
		game.state = Game.State.STARTED;
		game.boardState = new byte[columns][rows];
		game.mines = new HashSet<Pair<Integer,Integer>>(mines);
		while (game.mines.size() < mines) {
			int x = ThreadLocalRandom.current().nextInt(columns);
			int y = ThreadLocalRandom.current().nextInt(rows);
			game.mines.add(Pair.of(x, y));
		}
		return game;
	}

	private GameCellBoard createCell(Pair<Integer,Integer> coordinate) {
		int x = coordinate.getFirst();
		int y = coordinate.getSecond();
		if (this.mines.contains(coordinate)) {
			return new MineCellBoard(this, x, y, CellBoard.State.fromValue(this.boardState[x][y]));
		}
		return new NormalCellBoard(this, x, y, CellBoard.State.fromValue(this.boardState[x][y]));
	}
	
	private void setGameOver() {
		this.state = State.GAME_OVER;
		this.end = new Date();
	}
	
	private void setGameDone() {
		this.state = State.DONE;
		this.end = new Date();
	}
	
	private int countCoveredOrFlaggedCells() {
		int count = 0;
		for (int x=0; x < getColumns(); x++) {
			for (int y=0; y < getRows(); y++) {
				if (this.boardState[x][y] == CellBoard.State.COVERED.toValue() || 
					this.boardState[x][y] == CellBoard.State.FLAGGED.toValue()) {
					count++;
				}
			}
		}
		return count;
	}
	
	private void checkGameDone() {
		int coveredOrFlagged = countCoveredOrFlaggedCells();
		if (coveredOrFlagged == this.mines.size()) {
			setGameDone();
		}
	}
	
	protected Optional<GameCellBoard> getCell(int x, int y) {
		if (x >= 0 && x < getColumns() && y >= 0 && y < getRows()) {
			Pair<Integer,Integer> coordinate = Pair.of(x, y);
			if (this.cachedCellsBoards.containsKey(coordinate)) {
				return Optional.ofNullable(this.cachedCellsBoards.get(coordinate));
			}
			GameCellBoard cell = createCell(coordinate);
			this.cachedCellsBoards.put(coordinate, cell);
			return Optional.ofNullable(cell);
		}
		return Optional.ofNullable(null);
	}

	protected void cellStateChanged(CellBoard cell) {
		this.boardState[cell.getX()][cell.getY()] = cell.getState().toValue();
	}

	public CellBoard getCellBoard(int x, int y) {
		return getCell(x, y).orElseThrow(() -> new IndexOutOfBoundsException("Out of board"));
	}
	
	public void click(int column, int row) throws InvalidOperationException {
		if (!State.STARTED.equals(this.state)) {
			throw new InvalidOperationException("Game was ended up!");
		}
		try {
			getCell(column, row).orElseThrow(() -> new IndexOutOfBoundsException("Out of board") ).click();
			checkGameDone();
		} catch (GameOverException e) {
			setGameOver();
		}
	}
	
	public void flag(int column, int row) throws InvalidOperationException {
		if (!State.STARTED.equals(this.state)) {
			throw new InvalidOperationException("Game was ended up!");
		}
		getCell(column, row).orElseThrow(() -> new IndexOutOfBoundsException("Out of board") ).flag();
		checkGameDone();
	}
	
	public void unflag(int column, int row) throws InvalidOperationException {
		if (!State.STARTED.equals(this.state)) {
			throw new InvalidOperationException("Game was ended up!");
		}
		getCell(column, row).orElseThrow(() -> new IndexOutOfBoundsException("Out of board") ).unflag();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getColumns() {
		return this.boardState.length;
	}

	public Integer getRows() {
		return this.boardState[0].length;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}
	
	public State getState() {
		return this.state;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
