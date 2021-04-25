package com.matheusfelixr.sgcc.model.dto.UserAuthentication;

import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.operation.CreateOperationDTO;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Data
public class liteUserAuthenticationDTO {

    private Long id;

    private String userName;

    private String name;

    private String cpf;

    @Email
    private String email;

    private Boolean isAdmin;

    private Long idOperation;

    private Long idEmployeeWiseCall;

    public static UserAuthentication convertToEntity(liteUserAuthenticationDTO dto) {
        UserAuthentication ret = new UserAuthentication();
        ret.setId(dto.getId());
        ret.setUserName(dto.getUserName());
        ret.getEmployee().getPerson().setName(dto.getName());
        ret.getEmployee().getPerson().setCpf(dto.getCpf());
        ret.setEmail(dto.getEmail());
        ret.setIsAdmin(dto.getIsAdmin());
        ret.getEmployee().setOperation(new Operation(dto.getIdOperation()));
        ret.getEmployee().setIdEmployeeWiseCall(dto.getIdEmployeeWiseCall());
        return ret;
    }

    public static liteUserAuthenticationDTO convertToDTO(UserAuthentication entity) {
        liteUserAuthenticationDTO ret = new liteUserAuthenticationDTO();
        ret.setId(entity.getId());
        ret.setUserName(entity.getUserName());
        if(entity.getEmployee() != null && entity.getEmployee().getPerson() != null) {
            ret.setName(entity.getEmployee().getPerson().getName());
            ret.setCpf(entity.getEmployee().getPerson().getCpf());
        }
        ret.setEmail(entity.getEmail());
        ret.setIsAdmin(entity.getIsAdmin());
        if(entity.getEmployee() != null) {
            ret.setIdOperation(entity.getEmployee().getOperation().getId());
            ret.setIdEmployeeWiseCall(entity.getEmployee().getIdEmployeeWiseCall());
        }
        return ret;
    }

    public static List<liteUserAuthenticationDTO> convertToListDTO(List<UserAuthentication> entitys) {
        List<liteUserAuthenticationDTO> ret = new ArrayList<liteUserAuthenticationDTO>();
        for (UserAuthentication entity : entitys) {
            ret.add(liteUserAuthenticationDTO.convertToDTO(entity));
        }
        return ret;
    }

    public static List<UserAuthentication> convertToListEntity(List<liteUserAuthenticationDTO> DTOs) {
        List<UserAuthentication> ret = new ArrayList<UserAuthentication>();
        for (liteUserAuthenticationDTO dto : DTOs) {
            ret.add(liteUserAuthenticationDTO.convertToEntity(dto));
        }
        return ret;
    }


}
