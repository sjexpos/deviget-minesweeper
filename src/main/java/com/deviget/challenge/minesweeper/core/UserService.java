package com.deviget.challenge.minesweeper.core;

import com.deviget.challenge.minesweeper.core.exceptions.UserAlreadyExistsException;
import com.deviget.challenge.minesweeper.core.model.User;

public interface UserService {

	User newUser(String username, String password, String firstname, String lastname) throws UserAlreadyExistsException;
	
}
