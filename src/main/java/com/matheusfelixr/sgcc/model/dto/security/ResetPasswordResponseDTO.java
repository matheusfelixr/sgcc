package com.matheusfelixr.sgcc.model.dto.security;

import lombok.Data;

@Data
public class ResetPasswordResponseDTO {

	private String message;

	public ResetPasswordResponseDTO(String message) {
		this.message = message;
	}
}
