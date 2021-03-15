package com.deviget.challenge.minesweeper.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deviget.challenge.minesweeper.api.response.SessionInfoRespose;
import com.deviget.challenge.minesweeper.api.response.SignUpResponse;
import com.deviget.challenge.minesweeper.core.UserService;
import com.deviget.challenge.minesweeper.core.dao.UserRepository;
import com.deviget.challenge.minesweeper.core.exceptions.UserAlreadyExistsException;
import com.deviget.challenge.minesweeper.core.model.User;
import com.deviget.challenge.minesweeper.security.SessionManager;
import com.deviget.challenge.minesweeper.security.UserSession;

@RestController
@RequestMapping(Endpoints.SECURITY_ROOT_PATH)
public class SecurityEndpoint {
	private final Logger LOGGER = LoggerFactory.getLogger(SecurityEndpoint.class);
	
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	
	@PostMapping(Endpoints.SIGNIN_PATH)
	public SessionInfoRespose signIn(
			@RequestParam(name="username", required=true) String username,
			@RequestParam(name="password", required=true) String password
			) throws BadCredentialsException {
		
		User user = this.userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Bad credentials"));
		if (!user.getPassword().equals(password)) {
			throw new BadCredentialsException("Bad credentials");
		}
		UserSession session = this.sessionManager.newSession(user);
		LOGGER.info("SignIn was success!");
		return new SessionInfoRespose(session.getToken());
	}

	@PostMapping(Endpoints.SIGNOUT_PATH)
	public SessionInfoRespose signOut() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext.getAuthentication() != null && securityContext.getAuthentication().getPrincipal() instanceof UserSession) {
			UserSession session = (UserSession)securityContext.getAuthentication().getPrincipal();
			this.sessionManager.destroySession(session);
			new SessionInfoRespose(session.getToken());
		}
		return new SessionInfoRespose("");
	}

	@PostMapping(Endpoints.SIGNUP_PATH)
	public SignUpResponse signUp(
			@RequestParam(name="firstname", required=true) String firstname,
			@RequestParam(name="lastname", required=true) String lastname,
			@RequestParam(name="username", required=true) String username,
			@RequestParam(name="password", required=true) String password
			) throws UserAlreadyExistsException {
		User user = this.userService.newUser(username, password, firstname, lastname);
		return new SignUpResponse(user.getId(), user.getUsername());
	}
	
}
