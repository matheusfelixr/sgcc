package com.matheusfelixr.sgcc.controller;

import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.dto.config.ResponseApi;
import com.matheusfelixr.sgcc.model.dto.security.*;
import com.matheusfelixr.sgcc.service.SecurityService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/security")
@ApiOperation(value = "Controller responsável por controle de segurança da api. ")
public class SecurityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

	@Autowired
	private SecurityService securityService;

	@PostMapping(value  = "/authenticate")
	@ApiOperation(value = "Método responsável pela autenticação.")
	public ResponseEntity<ResponseApi<AuthenticateResponseDTO>> authenticate(@RequestBody AuthenticateRequestDTO authenticateRequestDTO, HttpServletRequest httpServletRequest) throws Exception {
		LOGGER.info("Inicio processo de autenticacao.");
		ResponseApi<AuthenticateResponseDTO> response = new ResponseApi<>();
		try {
			response.setData(this.securityService.authenticate(authenticateRequestDTO,httpServletRequest ));
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

	@PostMapping(value  = "/reset-password")
	@ApiOperation(value = "Método responsável por reset de senha.")
	public ResponseEntity<ResponseApi<MessageDTO>> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO, HttpServletRequest httpServletRequest) throws Exception {
		LOGGER.info("Inicio processo de reset de senha.");
		ResponseApi<MessageDTO> response = new ResponseApi<>();
		try {
			response.setData(this.securityService.resetPassword(resetPasswordRequestDTO.getUserName().trim(),httpServletRequest));
			LOGGER.info("Reset de senha realizado com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao tentar resetar senha");
			List<String> errors = Arrays.asList("Erro inesperado ao tentar resetar senha");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping(value  = "/create-user")
	@ApiOperation(value = "Método responsável por reset de senha.")
	public ResponseEntity<ResponseApi<MessageDTO>> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) throws Exception {
		LOGGER.info("Inicio processo de criação de usuario.");
		ResponseApi<MessageDTO> response = new ResponseApi<>();
		try {
			UserAuthentication currentUser = securityService.getCurrentUser();

			response.setData(this.securityService.createUser(createUserRequestDTO, currentUser));
			LOGGER.info("Processo de criação de usuario realizado com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (MailSendException e) {
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao enviar email com a senha.\n" + e.getMessage()+"\n"+ e.getCause());
			List<String> errors = Arrays.asList("Erro inesperado ao enviar email com a senha.");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao criar usuário a senha.");
			List<String> errors = Arrays.asList("Erro inesperado ao criar usuário.");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping(value  = "/new-password")
	@ApiOperation(value = "Método responsável por criar nova senha. Para usuarios que esteja com o campo para alterar senha.")
	public ResponseEntity<ResponseApi<AuthenticateResponseDTO>> newPassword(@RequestBody NewPasswordRequestDTO newPasswordRequestDTO, HttpServletRequest httpServletRequest) throws Exception {
		LOGGER.info("Inicio processo de criação de nova senha.");
		ResponseApi<AuthenticateResponseDTO> response = new ResponseApi<>();
		try {
			response.setData(this.securityService.newPassword(newPasswordRequestDTO, httpServletRequest));
			LOGGER.info("Criação de nova senha realizada com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao tentar criar senha");
			List<String> errors = Arrays.asList("Erro inesperado ao tentar criar senha");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}
}