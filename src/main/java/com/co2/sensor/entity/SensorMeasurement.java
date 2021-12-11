package com.co2.sensor.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "sensor_measurement")
@Data
public class SensorMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_measurement_id")
    private Long id;

    @Column(name = "co2_level",nullable = false)
    private Integer co2Level;

    @Column(name="created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;


}
