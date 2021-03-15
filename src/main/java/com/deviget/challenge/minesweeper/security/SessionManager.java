package com.deviget.challenge.minesweeper.security;

import com.deviget.challenge.minesweeper.core.model.User;

public interface SessionManager {

	UserSession getSession(String token);
	
	UserSession newSession(User user);
	
	void destroySession(UserSession session);
	
}
