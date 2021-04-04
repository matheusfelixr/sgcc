package com.matheusfelixr.sgcc.model.dto.operation;

import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.dto.cancellation.CancellationDTO;
import com.matheusfelixr.sgcc.model.dto.dataControl.DataControlDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateOperationDTO {

    private String description;

    public static Operation convertToEntity(CreateOperationDTO dto) {
        Operation ret = new Operation();
        ret.setDescription(dto.getDescription());
        return ret;
    }

    public static CreateOperationDTO convertToDTO(Operation entity) {
        CreateOperationDTO ret = new CreateOperationDTO();
        ret.setDescription(entity.getDescription());
        return ret;
    }

    public static List<CreateOperationDTO> convertToListDTO(List<Operation> entitys) {
        List<CreateOperationDTO> ret = new ArrayList<CreateOperationDTO>();
        for (Operation entity : entitys) {
            ret.add(CreateOperationDTO.convertToDTO(entity));
        }
        return ret;
    }

    public static List<Operation> convertToListEntity(List<CreateOperationDTO> DTOs) {
        List<Operation> ret = new ArrayList<Operation>();
        for (CreateOperationDTO dto : DTOs) {
            ret.add(CreateOperationDTO.convertToEntity(dto));
        }
        return ret;
    }

}
