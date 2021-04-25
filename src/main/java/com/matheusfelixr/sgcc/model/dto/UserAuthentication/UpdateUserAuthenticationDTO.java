package com.matheusfelixr.sgcc.model.dto.UserAuthentication;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UpdateUserAuthenticationDTO {

	private Long id;

	private String userName;

	private String name;

	private String cpf;

	@Email
	private String email;

	private Boolean isAdmin;

	private Long idOperation;

	private Long idEmployeeWiseCall;

}
