package com.matheusfelixr.sgcc.model.domain;

import com.matheusfelixr.sgcc.model.enums.ControlAccessPointEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "POINT_CONTROL")
@SequenceGenerator(name = "SEQ_POINT_CONTROL", sequenceName = "SEQ_POINT_CONTROL", allocationSize = 1)
public class PointControl {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POINT_CONTROL")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE", nullable = false)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NTP_DATE", nullable = true)
    private Date ntpDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "EMPLOYEE", referencedColumnName = "ID")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTROL_ACCESS_POINT", nullable = false)
    private ControlAccessPointEnum controlAccessPoint;

}
