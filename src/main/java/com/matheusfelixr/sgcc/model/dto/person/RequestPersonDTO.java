package com.matheusfelixr.sgcc.model.dto.person;

import com.matheusfelixr.sgcc.model.domain.Person;
import lombok.Data;


@Data
public class RequestPersonDTO {

    private String name;

    private String cpf;

    public static Person convertToEntity(RequestPersonDTO dto) {
        Person ret = new Person();
        ret.setName(dto.getName());
        ret.setCpf(dto.getCpf());

        return ret;
    }
}
