package com.matheusfelixr.sgcc.model.dto.operation;

import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.dto.cancellation.CancellationDTO;
import com.matheusfelixr.sgcc.model.dto.dataControl.DataControlDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OperationDTO {

    private Long id;

    private String description;

    private CancellationDTO cancellation;

    private DataControlDTO dataControl;


    public static Operation convertToEntity(OperationDTO dto) {
        Operation ret = new Operation();
        ret.setId(dto.getId());
        ret.setDescription(dto.getDescription());
        if (dto.getCancellation().getCancellationDate() != null) {
            ret.setCancellation(CancellationDTO.convertToEntity(dto.getCancellation()));
        }
        if (dto.getDataControl() != null) {
            ret.setDataControl(DataControlDTO.convertToEntity(dto.getDataControl()));
        }
        return ret;
    }

    public static OperationDTO convertToDTO(Operation entity) {
        OperationDTO ret = new OperationDTO();
        ret.setId(entity.getId());
        ret.setDescription(entity.getDescription());
        if (entity.getCancellation().getCancellationDate() != null) {
            ret.setCancellation(CancellationDTO.convertToDTO(entity.getCancellation()));
        }
        if (entity.getDataControl() != null) {
            ret.setDataControl(DataControlDTO.convertToDTO(entity.getDataControl()));
        }
        return ret;
    }

    public static List<OperationDTO> convertToListDTO(List<Operation> entitys) {
        List<OperationDTO> ret = new ArrayList<OperationDTO>();
        for (Operation entity : entitys) {
            ret.add(OperationDTO.convertToDTO(entity));
        }
        return ret;
    }

    public static List<Operation> convertToListEntity(List<OperationDTO> DTOs) {
        List<Operation> ret = new ArrayList<Operation>();
        for (OperationDTO dto : DTOs) {
            ret.add(OperationDTO.convertToEntity(dto));
        }
        return ret;
    }

}
