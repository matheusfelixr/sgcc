package com.matheusfelixr.sgcc.controller;

import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.dto.config.ResponseApi;
import com.matheusfelixr.sgcc.model.dto.pointControl.PointControlResponseHadEntry;
import com.matheusfelixr.sgcc.service.PointControlService;
import com.matheusfelixr.sgcc.service.SecurityService;
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
@RequestMapping(path = "/point-control")
@ApiOperation(value = "Controller responsável pelo controle de ponto ")
public class PointControlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointControlController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PointControlService pointControlService;

    @GetMapping(value = "/registry-point")
    @ApiOperation(value = "Método responsável por registrar ponto.")
    public ResponseEntity<ResponseApi<MessageDTO>> registryPoint(HttpServletRequest httpServletRequest) throws Exception {
        LOGGER.debug("Inicio processo de registro um novo ponto.");
        ResponseApi<MessageDTO> response = new ResponseApi<>();
        try {
            UserAuthentication currentUser = securityService.getCurrentUser();

            response.setData(this.pointControlService.registryPoint(currentUser, httpServletRequest));
            LOGGER.debug("Registro realizado com sucesso.");
            return ResponseEntity.ok(response);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            response.setErrors(Arrays.asList(e.getMessage()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Erro inesperado ao realizar novo ponto");
            List<String> errors = Arrays.asList("Erro inesperado ao realizar novo ponto");
            response.setErrors(errors);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(value = "/had-entry")
    @ApiOperation(value = "Método responsável por verificar se já teve entrada de ponto no dia.")
    public ResponseEntity<ResponseApi<PointControlResponseHadEntry>> hadEntry() throws Exception {
        LOGGER.debug("Inicio processo de verificar se já teve entrada de ponto no dia.");
        ResponseApi<PointControlResponseHadEntry> response = new ResponseApi<>();
        try {
            PointControlResponseHadEntry ret = new PointControlResponseHadEntry();
            UserAuthentication currentUser = securityService.getCurrentUser();

            ret.setHadEntry(this.pointControlService.itWasAnEntryLast(currentUser));

            response.setData(ret);
            LOGGER.debug("Verificação concluida se já teve entrada de ponto no dia.");
            return ResponseEntity.ok(response);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            response.setErrors(Arrays.asList(e.getMessage()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Erro inesperado ao verificar se já teve entrada de ponto no dia.");
            List<String> errors = Arrays.asList("Erro inesperado ao verificar se já teve entrada de ponto no dia.");
            response.setErrors(errors);
            return ResponseEntity.ok(response);
        }
    }


}