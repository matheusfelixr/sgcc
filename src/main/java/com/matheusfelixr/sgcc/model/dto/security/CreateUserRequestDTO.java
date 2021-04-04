package com.matheusfelixr.sgcc.model.dto.security;

import lombok.Data;

@Data
public class CreateUserRequestDTO {

	private String userName;

	private String name;

	private String cpf;

	private String email;

	private String password;

	private Boolean isAdmin;

	private Long idOperation;

}
