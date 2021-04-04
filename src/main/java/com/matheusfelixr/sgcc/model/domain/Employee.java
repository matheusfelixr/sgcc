package com.matheusfelixr.sgcc.model.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "EMPLOYEE")
@SequenceGenerator(name = "SEQ_EMPLOYEE", sequenceName = "SEQ_EMPLOYEE", allocationSize = 1)
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EMPLOYEE")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSON", referencedColumnName = "ID")
    private Person person;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "OPERATION", referencedColumnName = "ID")
    private Operation operation;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Embedded
    private DataControlImpl dataControl;

    public DataControlImpl getDataControl() {
        if (this.dataControl == null) {
            dataControl = new DataControlImpl();
        }
        return dataControl;
    }


}
