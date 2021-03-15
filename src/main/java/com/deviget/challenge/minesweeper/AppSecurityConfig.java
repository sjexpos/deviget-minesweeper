package com.deviget.challenge.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import com.deviget.challenge.minesweeper.api.Endpoints;
import com.deviget.challenge.minesweeper.security.SecurityTokenFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		SecurityTokenFilter filter = new SecurityTokenFilter();
		filter.setAuthenticationManager(this.authenticationManager);
		
		http.csrf().disable()
			.addFilter(filter)
			.addFilterBefore(new ExceptionTranslationFilter(new Http403ForbiddenEntryPoint()), filter.getClass())
			.authorizeRequests()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/actuator/**").permitAll()
			.antMatchers(Endpoints.SECURITY_ROOT_PATH+Endpoints.SIGNIN_PATH).permitAll()
			.antMatchers(Endpoints.SECURITY_ROOT_PATH+Endpoints.SIGNUP_PATH).permitAll()
			.antMatchers(Endpoints.ROOT+"/**").authenticated()
			.filterSecurityInterceptorOncePerRequest(true);
	}

}
