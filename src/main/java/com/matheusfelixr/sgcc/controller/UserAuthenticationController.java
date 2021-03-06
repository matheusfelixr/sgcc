package com.matheusfelixr.sgcc.controller;

import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.dto.config.ResponseApi;
import com.matheusfelixr.sgcc.model.dto.security.*;
import com.matheusfelixr.sgcc.service.SecurityService;
import com.matheusfelixr.sgcc.service.UserAuthenticationService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/user-authentication")
@ApiOperation(value = "Controller responsável por controle da entidade userAuthentication. ")
public class UserAuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationController.class);

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserAuthenticationService userAuthenticationService;

	@PutMapping(value  = "/cancel/{idUser}/{observation}")
	@ApiOperation(value = "Método responsável por cancelar um usuario.")
	public ResponseEntity<ResponseApi<MessageDTO>> cancel(@PathVariable(value = "idUser") Long idUser, @PathVariable(value = "observation") String observation){
		LOGGER.info("Inicio processo de cancelamento de um usuario");
		ResponseApi<MessageDTO> response = new ResponseApi<>();
		try {
			UserAuthentication currentUser = securityService.getCurrentUser();

			response.setData(this.userAuthenticationService.cancel(idUser, observation, currentUser));
			LOGGER.info("Autenticacao realizada com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao tentar autenticar");
			List<String> errors = Arrays.asList("Erro inesperado ao tentar autenticar");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}

	@PutMapping(value  = "/remove-cancel/{idUser}")
	@ApiOperation(value = "Método responsável por cancelar um usuario.")
	public ResponseEntity<ResponseApi<MessageDTO>> removeCancel(@PathVariable(value = "idUser") Long idUser){
		LOGGER.info("Inicio processo de cancelamento de um usuario");
		ResponseApi<MessageDTO> response = new ResponseApi<>();
		try {
			UserAuthentication currentUser = securityService.getCurrentUser();

			response.setData(this.userAuthenticationService.removeCancel(idUser, currentUser));
			LOGGER.info("Autenticacao realizada com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao tentar autenticar");
			List<String> errors = Arrays.asList("Erro inesperado ao tentar autenticar");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}
}