package com.deviget.challenge.minesweeper.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.deviget.challenge.minesweeper.core.dao.UserRepository;
import com.deviget.challenge.minesweeper.core.model.User;
import com.deviget.challenge.minesweeper.security.UserSession;

public abstract class AbstractEndpoints {
	
	@Autowired
	protected UserRepository userRepository;;
	
	protected Optional<UserSession> currentSession() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext.getAuthentication() != null && securityContext.getAuthentication().getPrincipal() instanceof UserSession) {
			return Optional.ofNullable((UserSession)securityContext.getAuthentication().getPrincipal());
		}
		return Optional.ofNullable(null);
	}
	
	protected Optional<User> currentUser() {
		UserSession session = currentSession().orElseThrow(() -> new RuntimeException("Session not available!") );
		return this.userRepository.findById(session.getUserId());
	}

}
