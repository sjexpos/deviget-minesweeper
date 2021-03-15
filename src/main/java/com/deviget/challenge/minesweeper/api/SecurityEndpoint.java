package com.deviget.challenge.minesweeper.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(Endpoints.SECURITY_ROOT_PATH)
@Validated
public class SecurityEndpoint extends AbstractEndpoints {
	private final Logger LOGGER = LoggerFactory.getLogger(SecurityEndpoint.class);
	
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	@ApiOperation(value="Sign in on application and get a secure token to access")
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="username", value="Username"),
			@ApiImplicitParam(name="password", value="password")
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Sign-In successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=401, message="If the user can not be authenticated", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
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

	@ApiOperation(value="Sign out from the application")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Sign out successful"),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
	@PostMapping(Endpoints.SIGNOUT_PATH)
	public SessionInfoRespose signOut() {
		UserSession session = currentSession().orElse(new UserSession());
		this.sessionManager.destroySession(session);
		return new SessionInfoRespose(session.getToken());
	}

	@ApiOperation(value="Create a new user/account into the application")
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="firstname", value="First name"),
			@ApiImplicitParam(name="lastname", value="Last name"),
			@ApiImplicitParam(name="username", value="Username"),
			@ApiImplicitParam(name="password", value="Password")
	})
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Sign out successful"),
			@ApiResponse(code=400, message="If there is any error on parameter validations (types, values, range, etc.)", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=403, message="If the request does not have security header X-Auth-Token", response=GlobalExceptionHandler.ExceptionResponse.class),
			@ApiResponse(code=409, message="If username already exists", response=GlobalExceptionHandler.ExceptionResponse.class)
	})
	@PostMapping(Endpoints.SIGNUP_PATH)
	public SignUpResponse signUp(
			@RequestParam(name="firstname", required=true) String firstname,
			@RequestParam(name="lastname", required=true) String lastname,
			@RequestParam(name="username", required=true) @NotBlank @Size(min=5) String username,
			@RequestParam(name="password", required=true) @NotBlank @Size(min=8) String password
			) throws UserAlreadyExistsException {
		User user = this.userService.newUser(username, password, firstname, lastname);
		return new SignUpResponse(user.getId(), user.getUsername());
	}
	
}
