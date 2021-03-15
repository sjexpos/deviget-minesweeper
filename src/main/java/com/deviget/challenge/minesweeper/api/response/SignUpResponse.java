package com.deviget.challenge.minesweeper.api.response;

import io.swagger.annotations.ApiModelProperty;

public class SignUpResponse {
	@ApiModelProperty(value="User identifier")
	private String userId;
	private String username;

	public SignUpResponse(String userId, String username) {
		super();
		this.userId = userId;
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

}
