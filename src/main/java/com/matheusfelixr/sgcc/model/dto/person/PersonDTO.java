package com.matheusfelixr.sgcc.model.dto.person;

import com.matheusfelixr.sgcc.model.domain.Person;
import com.matheusfelixr.sgcc.model.dto.cancellation.CancellationDTO;
import com.matheusfelixr.sgcc.model.dto.dataControl.DataControlDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonDTO {

    private Long id;

    private String name;

    private String cpf;

    private CancellationDTO cancellation;

    private DataControlDTO dataControl;

    public static Person convertToEntity(PersonDTO dto) {
        Person ret = new Person();
        ret.setName(dto.getName());
        ret.setCpf(dto.getCpf());
        if (dto.getCancellation().getCancellationDate() != null) {
            ret.setCancellation(CancellationDTO.convertToEntity(dto.getCancellation()));
        }
        if (dto.getDataControl() != null) {
            ret.setDataControl(DataControlDTO.convertToEntity(dto.getDataControl()));
        }
        return ret;
    }

    public static PersonDTO convertToDTO(Person entity) {
        PersonDTO ret = new PersonDTO();
        ret.setId(entity.getId());
        ret.setName(entity.getName());
        ret.setCpf(entity.getCpf());
        if (entity.getCancellation().getCancellationDate() != null) {
            ret.setCancellation(CancellationDTO.convertToDTO(entity.getCancellation()));
        }
        if (entity.getDataControl() != null) {
            ret.setDataControl(DataControlDTO.convertToDTO(entity.getDataControl()));
        }
        return ret;
    }

    public static List<PersonDTO> convertToListDTO(List<Person> entitys) {
        List<PersonDTO> ret = new ArrayList<PersonDTO>();
        for (Person entity : entitys) {
            ret.add(PersonDTO.convertToDTO(entity));
        }
        return ret;
    }

    public static List<Person> convertToListEntity(List<PersonDTO> DTOs) {
        List<Person> ret = new ArrayList<Person>();
        for (PersonDTO dto : DTOs) {
            ret.add(PersonDTO.convertToEntity(dto));
        }
        return ret;
    }

}
