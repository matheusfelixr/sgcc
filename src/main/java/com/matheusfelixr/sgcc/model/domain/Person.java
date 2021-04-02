package com.matheusfelixr.sgcc.model.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PERSON" )
@SequenceGenerator(name = "SEQ_PERSON", sequenceName = "SEQ_PERSON", allocationSize = 1)
public class Person {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERSON")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CPF", nullable = false)
    private String cpf;

    @Embedded
    private CancellationImpl cancellation;

    @Embedded
    private DataControlImpl dataControl;

}
