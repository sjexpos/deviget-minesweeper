package com.deviget.challenge.minesweeper.core.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.deviget.challenge.minesweeper.core.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);
	
}
