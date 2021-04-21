package com.matheusfelixr.sgcc.model.dto.security;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class CreateUserRequestDTO {

	private String userName;

	private String name;

	private String cpf;

	@Email
	private String email;

	private String password;

	private Boolean isAdmin;

	private Long idOperation;

	private Long idEmployeeWiseCall;

}
