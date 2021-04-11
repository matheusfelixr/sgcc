package com.matheusfelixr.sgcc.model.dto.security;

import lombok.Data;

import java.util.Locale;

@Data
public class AuthenticateRequestDTO {
	
	private String userName;
	private String password;

	public AuthenticateRequestDTO() {
	}

	public AuthenticateRequestDTO(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

}
