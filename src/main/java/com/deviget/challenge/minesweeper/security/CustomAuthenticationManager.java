package com.deviget.challenge.minesweeper.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationManager implements AuthenticationManager {
	static private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationManager.class);

	@Autowired
	private SessionManager sessionManager;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			if (authentication.getPrincipal() instanceof String) {
				String token = (String)authentication.getPrincipal();
				UserSession session = this.sessionManager.getSession(token);
				if (session == null) {
					throw new BadCredentialsException("Failed to get session");
				}
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(session, null, null);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				return usernamePasswordAuthenticationToken;
			}
		} catch (Exception exception) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info("ERROR: ", exception);
			} else {
				LOGGER.info(String.format("ERROR: %s [%s] ", exception.getClass().getSimpleName(), exception.getMessage()));
			}
			throw new BadCredentialsException("Invalid Token");
		}
		throw new BadCredentialsException("Invalid Token");
	}

}
