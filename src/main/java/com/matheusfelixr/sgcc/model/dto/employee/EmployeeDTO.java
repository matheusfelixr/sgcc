package com.matheusfelixr.sgcc.model.dto.employee;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.dto.dataControl.DataControlDTO;
import com.matheusfelixr.sgcc.model.dto.operation.OperationDTO;
import com.matheusfelixr.sgcc.model.dto.person.PersonDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeDTO {

    private Long id;

    private PersonDTO person;

    private OperationDTO operation;

    private Boolean active;

    private DataControlDTO dataControl;

    public static Employee convertToEntity(EmployeeDTO dto) {
        Employee ret = new Employee();
        ret.setPerson(PersonDTO.convertToEntity(dto.getPerson()));
        ret.setOperation(OperationDTO.convertToEntity(dto.getOperation()));
        ret.setActive(dto.getActive());
        if (dto.getDataControl() != null) {
            ret.setDataControl(DataControlDTO.convertToEntity(dto.getDataControl()));
        }
        return ret;
    }

    public static EmployeeDTO convertToDTO(Employee entity) {
        EmployeeDTO ret = new EmployeeDTO();
        ret.setId(entity.getId());
        ret.setPerson(PersonDTO.convertToDTO(entity.getPerson()));
        ret.setOperation(OperationDTO.convertToDTO(entity.getOperation()));
        ret.setActive(entity.getActive());
        if (entity.getDataControl() != null) {
            ret.setDataControl(DataControlDTO.convertToDTO(entity.getDataControl()));
        }
        return ret;
    }

    public static List<EmployeeDTO> convertToListDTO(List<Employee> entitys) {
        List<EmployeeDTO> ret = new ArrayList<EmployeeDTO>();
        for (Employee entity : entitys) {
            ret.add(EmployeeDTO.convertToDTO(entity));
        }
        return ret;
    }

    public static List<Employee> convertToListEntity(List<EmployeeDTO> DTOs) {
        List<Employee> ret = new ArrayList<Employee>();
        for (EmployeeDTO dto : DTOs) {
            ret.add(EmployeeDTO.convertToEntity(dto));
        }
        return ret;
    }

}
