package com.co2.sensor.service;

import com.co2.sensor.entity.Sensor;
import com.co2.sensor.entity.SensorAlerts;
import com.co2.sensor.entity.SensorMeasurement;
import com.co2.sensor.model.SensorDTO;
import com.co2.sensor.repository.SensorAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorAlertServiceImpl implements SensorAlertService
{
    SensorAlertRepository sensorAlertRepository;

    @Autowired
    public SensorAlertServiceImpl(SensorAlertRepository sensorAlertRepository)
    {
        this.sensorAlertRepository = sensorAlertRepository;
    }


    @Override
    public void createAlert(Sensor sensor, List<SensorMeasurement> sensorMeasurements,SensorDTO sensorDTO)
    {
        SensorAlerts sensorAlerts = new SensorAlerts();
        sensorAlerts.setSensor(sensor);
        sensorAlerts.setStartTime(sensorMeasurements.get(sensorMeasurements.size() - 1).getCreatedDate());
        StringBuilder co2levels=new StringBuilder();
        for (SensorMeasurement sensorMeasurement : sensorMeasurements)
        {
            co2levels.append(sensorMeasurement.getCo2Level());
            co2levels.append(",");
        }
        co2levels.append(sensorDTO.getCo2Level());
        sensorAlerts.setCo2levels(co2levels.toString());
        sensorAlerts.setEndTime(sensorDTO.getDate());
        sensorAlertRepository.save(sensorAlerts);
    }


    @Override
    public List<Map<String,String>> getSensorAlerts(String uuid)
    {
        List<Map<String,String>> alerts=new ArrayList<>();
        List<SensorAlerts> sensorAlerts=sensorAlertRepository.getAlerts(uuid);

        for (SensorAlerts alert:sensorAlerts)
        {
            Map<String,String> alertData=new HashMap<>();
            alertData.put("startTime",alert.getStartTime().toString());
            alertData.put("endTime",alert.getEndTime().toString());
            String[] co2levels=alert.getCo2levels().split(",");
            for (int i = 0; i < co2levels.length; i++)
            {
                alertData.put("measurement"+(i+1),co2levels[i]);
            }
            alerts.add(alertData);

        }
        return alerts;
    }
}
