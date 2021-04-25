package com.matheusfelixr.sgcc.model.dto.security;

import lombok.Data;

@Data
public class EditPasswordRequestDTO {

	private Long idUser;
	private String password;

}
