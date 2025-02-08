package com.e_commerce.dto;

import com.e_commerce.entity.User;
import lombok.Data;

@Data
public class AuthResponse {

	private String token;
	private String message;
	private String role;
	private User user;

	public AuthResponse() {

	}

	public AuthResponse(String token, String message, String role, User user) {
		super();
		this.token = token;
		this.message = message;
		this.role = role;
		this.user = user;
	}

}
