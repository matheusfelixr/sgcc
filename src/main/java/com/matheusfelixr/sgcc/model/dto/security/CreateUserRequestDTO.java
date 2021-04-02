package com.matheusfelixr.sgcc.model.dto.security;

import lombok.Data;

@Data
public class CreateUserRequestDTO {

	private String username;

	private String email;

	private String password;

	private Boolean isAdmin;

}
