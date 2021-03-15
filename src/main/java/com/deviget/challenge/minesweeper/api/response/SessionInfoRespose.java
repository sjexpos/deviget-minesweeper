package com.deviget.challenge.minesweeper.api.response;

import io.swagger.annotations.ApiModelProperty;

public class SessionInfoRespose {
	@ApiModelProperty(value="Secure token. It must be used on header X-Auth-Token")
	private String token;

	public SessionInfoRespose(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
