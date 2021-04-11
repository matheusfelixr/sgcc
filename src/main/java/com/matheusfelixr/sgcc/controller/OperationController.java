package com.matheusfelixr.sgcc.controller;

import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.config.ResponseApi;
import com.matheusfelixr.sgcc.model.dto.operation.CreateOperationDTO;
import com.matheusfelixr.sgcc.model.dto.operation.OperationDTO;
import com.matheusfelixr.sgcc.service.OperationService;
import com.matheusfelixr.sgcc.service.SecurityService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;
@RestController
@CrossOrigin
@RequestMapping(path = "/operation")
@ApiOperation(value = "Controller pelo controle de operações. ")
public class OperationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);

	@Autowired
	private SecurityService securityService;

	@Autowired
	private OperationService operationService;

	@PostMapping(value  = "/create")
	@ApiOperation(value = "Método responsável criar uma operação.")
	public ResponseEntity<ResponseApi<OperationDTO>> authenticate(@RequestBody CreateOperationDTO createOperationDTO) throws Exception {
		LOGGER.debug("Inicio processo de criar uma operação.");
		ResponseApi<OperationDTO> response = new ResponseApi<>();
		try {
			UserAuthentication currentUser = securityService.getCurrentUser();

			Operation operation = this.operationService.create(CreateOperationDTO.convertToEntity(createOperationDTO), currentUser);

			response.setData(OperationDTO.convertToDTO(operation));
			LOGGER.debug("Processo de criar uma operação realizada com sucesso.");
			return ResponseEntity.ok(response);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			response.setErrors(Arrays.asList(e.getMessage()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			LOGGER.error("Erro inesperado ao criar operação");
			List<String> errors = Arrays.asList("Erro inesperado ao criar operação");
			response.setErrors(errors);
			return ResponseEntity.ok(response);
		}
	}
}