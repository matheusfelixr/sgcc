package com.matheusfelixr.sgcc.controller;

import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.dto.UserAuthentication.UpdateUserAuthenticationDTO;
import com.matheusfelixr.sgcc.model.dto.UserAuthentication.liteUserAuthenticationDTO;
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
			LOGGER.info("Cancelamento realizada com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao tentar cancelar um usuario");
			List<String> errors = Arrays.asList("Erro inesperado ao tentar cancelar um usuario");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}

	@PutMapping(value  = "/update-user")
	@ApiOperation(value = "Método responsável por editar um usuario.")
	public ResponseEntity<ResponseApi<MessageDTO>> editUser(@RequestBody UpdateUserAuthenticationDTO updateUserAuthenticationDTO){
		LOGGER.info("Inicio processo de editar um usuario");
		ResponseApi<MessageDTO> response = new ResponseApi<>();
		try {
			UserAuthentication currentUser = securityService.getCurrentUser();

			response.setData(this.userAuthenticationService.update(updateUserAuthenticationDTO, currentUser));
			LOGGER.info("Sucesso ao editar usuario.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao editar usuario");
			List<String> errors = Arrays.asList("Erro inesperado ao editar usuario");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}


	@GetMapping(value  = "/find-by-id/{idUser}")
	@ApiOperation(value = "Método responsável por encontrar usuario por id.")
	public ResponseEntity<ResponseApi<liteUserAuthenticationDTO>> editUser(@PathVariable(value = "idUser") Long idUser){
		LOGGER.info("Inicio processo de editar um usuario");
		ResponseApi<liteUserAuthenticationDTO> response = new ResponseApi<>();
		try {
			response.setData(liteUserAuthenticationDTO.convertToDTO(this.userAuthenticationService.findById(idUser).get()));
			LOGGER.info("Sucesso ao editar usuario.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao editar usuario");
			List<String> errors = Arrays.asList("Erro inesperado ao editar usuario");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}

	@GetMapping(value  = "/find-all")
	@ApiOperation(value = "Método responsável por buscar todos usuarios.")
	public ResponseEntity<ResponseApi<List<liteUserAuthenticationDTO>>> findAll(){
		LOGGER.info("Inicio processo de buscar todos usuarios");
		ResponseApi<List<liteUserAuthenticationDTO>> response = new ResponseApi<>();
		try {
			response.setData(liteUserAuthenticationDTO.convertToListDTO(this.userAuthenticationService.findAll()));
			LOGGER.info("Sucesso ao buscar todos usuarios.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao buscar todos usuarios");
			List<String> errors = Arrays.asList("Erro inesperado ao buscar todos usuarios");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}
}