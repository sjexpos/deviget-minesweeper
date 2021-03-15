package com.deviget.challenge.minesweeper.core;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deviget.challenge.minesweeper.core.dao.UserRepository;
import com.deviget.challenge.minesweeper.core.exceptions.UserAlreadyExistsException;
import com.deviget.challenge.minesweeper.core.model.User;

@Service
public class UserServiceImpl implements UserService {
	private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	public UserServiceImpl() {
		super();
	}

	public User newUser(String username, String password, String firstname, String lastname) throws UserAlreadyExistsException {
		
		if (this.userRepository.findByUsername(username).isPresent()) {
			throw new UserAlreadyExistsException(username);
		}
		
		User user = new User();
		user.setId("user::"+UUID.randomUUID().toString());
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setUsername(username);
		user.setPassword(password);
		this.userRepository.save(user);
		return user;
	}

	
}
