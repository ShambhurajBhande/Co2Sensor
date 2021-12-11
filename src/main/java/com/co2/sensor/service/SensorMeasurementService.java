package com.co2.sensor.service;

import com.co2.sensor.model.SensorDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public interface SensorMeasurementService
{
    void saveMeasurement(String uuid, SensorDTO sensorDTO);

    Map<String, String> getSensorMetrics(String uuid);
}
