package com.matheusfelixr.sgcc.model.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "EMPLOYEE" )
@SequenceGenerator(name = "SEQ_EMPLOYEE", sequenceName = "SEQ_EMPLOYEE", allocationSize = 1)
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EMPLOYEE")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE", referencedColumnName = "ID")
    private Person person;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Embedded
    private DataControlImpl dataControl;

}
