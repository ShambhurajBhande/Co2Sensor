package com.co2.sensor.entity;

import com.co2.sensor.enums.SensorStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "sensor")
@Data
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sensor_uuid",nullable = false)
    private String uuid;


    @Column(name = "sensor_status")
    @Enumerated(EnumType.STRING)
    private SensorStatus sensorStatus =SensorStatus.OK;

    @Column(name="last_updated_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

}
