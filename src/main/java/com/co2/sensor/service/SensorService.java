package com.co2.sensor.service;

import com.co2.sensor.entity.Sensor;
import org.springframework.stereotype.Service;

@Service
public interface SensorService
{
    Sensor findByUuid(final String uuid);

    Sensor save(Sensor sensorNew);

}
