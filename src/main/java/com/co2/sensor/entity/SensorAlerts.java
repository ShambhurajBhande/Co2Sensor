package com.co2.sensor.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sensor_alerts")
@Data
public class SensorAlerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="co2_levels")
    private String co2levels;

    @Column(name="start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name="end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

}
