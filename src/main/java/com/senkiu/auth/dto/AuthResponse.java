package com.senkiu.auth.dto;

public class AuthResponse {
	private final String email;
	private final String rol;

	public AuthResponse(String email, String rol) {
		this.email = email;
		this.rol = rol;
	}

	public String getEmail() {
		return email;
	}

	public String getRol() {
		return rol;
	}
}