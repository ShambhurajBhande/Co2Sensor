package com.co2.sensor.service;

import com.co2.sensor.entity.Sensor;
import com.co2.sensor.entity.SensorMeasurement;
import com.co2.sensor.model.SensorDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SensorAlertService
{
    void createAlert(Sensor sensor, List<SensorMeasurement> sensorMeasurements, SensorDTO sensorDTO);

    List<Map<String,String>> getSensorAlerts(String uuid);
}
