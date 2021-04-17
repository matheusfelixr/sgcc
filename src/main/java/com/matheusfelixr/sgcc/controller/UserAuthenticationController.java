package com.matheusfelixr.sgcc.controller;

import com.matheusfelixr.sgcc.model.domain.Person;
import com.matheusfelixr.sgcc.model.dto.config.ResponseApi;
import com.matheusfelixr.sgcc.model.dto.person.RequestPersonDTO;
import com.matheusfelixr.sgcc.model.dto.userAuthentication.AllUserAtributesDTO;
import com.matheusfelixr.sgcc.service.PersonService;
import com.matheusfelixr.sgcc.service.UserAuthenticationService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/user-authentication")
@ApiOperation(value = "Controller pelo controle de autenticação do usuário. ")
public class UserAuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationController.class);

	@Autowired
	private PersonService personService;

	@Autowired
	private UserAuthenticationService userAuthenticationService;

	@ApiOperation(value = "Metodo responsavel por buscar todos usuários cancelados ou ativos de acordo com o parametro recebido.")
	@GetMapping(value = "/find-by-example")
	public ResponseEntity<ResponseApi<AllUserAtributesDTO>> findByExampleAndStatus(@RequestBody RequestPersonDTO requestPersonDTO) {
		ResponseApi<AllUserAtributesDTO> response = new ResponseApi<>();
		try {
			Optional<List<Person>> persons = this.personService.findByExample(RequestPersonDTO.convertToEntity(requestPersonDTO));

			if (!persons.isPresent()) {
				List<String> errors = Arrays.asList("Erro ao encontrar usuários.");
				response.setErrors(errors);
				LOGGER.error("Não foi possivel encontrar usuários. ");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			AllUserAtributesDTO allUserAtributesDTO = this.userAuthenticationService.generateAllUserAtributesByPerson(persons);
			response.setData(allUserAtributesDTO);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Internal Error: " + e.getCause());
			List<String> erros = Arrays.asList("Erro ao encontrar usuários.");
			response.setErrors(erros);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return ResponseEntity.ok().body(response);
	}
}