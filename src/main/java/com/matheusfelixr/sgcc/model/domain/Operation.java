package com.matheusfelixr.sgcc.model.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "OPERATION")
@SequenceGenerator(name = "SEQ_OPERATION", sequenceName = "SEQ_OPERATION", allocationSize = 1)
public class Operation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OPERATION")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    private CancellationImpl cancellation;

    @Embedded
    private DataControlImpl dataControl;

    public CancellationImpl getCancellation() {
        if(this.cancellation == null){
            cancellation = new CancellationImpl();
        }
        return cancellation;
    }

    public DataControlImpl getDataControl() {
        if (this.dataControl == null) {
            dataControl = new DataControlImpl();
        }
        return dataControl;
    }


}
