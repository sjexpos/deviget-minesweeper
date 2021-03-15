package com.deviget.challenge.minesweeper.core.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.deviget.challenge.minesweeper.core.model.Game;
import com.deviget.challenge.minesweeper.core.model.User;

public interface GameRepository  extends MongoRepository<Game, String> {

	List<Game> findByUser(User user);
	
}
