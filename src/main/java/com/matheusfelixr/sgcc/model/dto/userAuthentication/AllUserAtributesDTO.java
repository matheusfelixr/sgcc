package com.matheusfelixr.sgcc.model.dto.userAuthentication;

import com.matheusfelixr.sgcc.model.dto.cancellation.CancellationDTO;
import com.matheusfelixr.sgcc.model.dto.dataControl.DataControlDTO;
import com.matheusfelixr.sgcc.model.dto.employee.EmployeeDTO;
import com.matheusfelixr.sgcc.model.dto.operation.OperationDTO;
import com.matheusfelixr.sgcc.model.dto.person.PersonDTO;
import lombok.Data;

@Data
public class AllUserAtributesDTO {

    private UserAuthenticationDTO userAuthentication;

    private PersonDTO person;

    private EmployeeDTO employee;

    private OperationDTO operation;

    public AllUserAtributesDTO(UserAuthenticationDTO userAuthentication, PersonDTO person, EmployeeDTO employee, OperationDTO operation) {
        this.userAuthentication = userAuthentication;
        this.person = person;
        this.employee = employee;
        this.operation = operation;
    }
}
