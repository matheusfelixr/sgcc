package com.matheusfelixr.sgcc.model.dto.userAuthentication;

import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.cancellation.CancellationDTO;
import com.matheusfelixr.sgcc.model.dto.employee.EmployeeDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserAuthenticationDTO {

    private Long id;

    private String userName;

    private String password;

    private String email;

    private Boolean changePassword;

    private Boolean isAdmin;

    private EmployeeDTO employee;

    private CancellationDTO cancellation;

    public static UserAuthentication convertToEntity(UserAuthenticationDTO dto) {
        UserAuthentication ret = new UserAuthentication();
        ret.setUserName(dto.getUserName());
        ret.setPassword(dto.getPassword());
        ret.setEmail(dto.getEmail());
        ret.setChangePassword(dto.getChangePassword());
        ret.setIsAdmin(dto.getIsAdmin());
        ret.setEmployee(EmployeeDTO.convertToEntity(dto.getEmployee()));
        if (dto.getCancellation().getCancellationDate() != null) {
            ret.setCancellation(CancellationDTO.convertToEntity(dto.getCancellation()));
        }
        return ret;
    }

    public static UserAuthenticationDTO convertToDTO(UserAuthentication entity) {
        UserAuthenticationDTO ret = new UserAuthenticationDTO();
        ret.setId(entity.getId());
        ret.setUserName(entity.getUserName());
        ret.setPassword(entity.getPassword());
        ret.setEmail(entity.getEmail());
        ret.setChangePassword(entity.getChangePassword());
        ret.setIsAdmin(entity.getIsAdmin());
        ret.setEmployee(EmployeeDTO.convertToDTO(entity.getEmployee()));
        if (entity.getCancellation().getCancellationDate() != null) {
            ret.setCancellation(CancellationDTO.convertToDTO(entity.getCancellation()));
        }

        return ret;
    }

    public static List<UserAuthenticationDTO> convertToListDTO(List<UserAuthentication> entitys) {
        List<UserAuthenticationDTO> ret = new ArrayList<UserAuthenticationDTO>();
        for (UserAuthentication entity : entitys) {
            ret.add(UserAuthenticationDTO.convertToDTO(entity));
        }
        return ret;
    }

    public static List<UserAuthentication> convertToListEntity(List<UserAuthenticationDTO> DTOs) {
        List<UserAuthentication> ret = new ArrayList<UserAuthentication>();
        for (UserAuthenticationDTO dto : DTOs) {
            ret.add(UserAuthenticationDTO.convertToEntity(dto));
        }
        return ret;
    }
}
