package com.co2.sensor.service;

import com.co2.sensor.entity.Sensor;
import com.co2.sensor.entity.SensorMeasurement;
import com.co2.sensor.enums.SensorStatus;
import com.co2.sensor.model.SensorDTO;
import com.co2.sensor.repository.SensorMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorMeasurementServiceImpl implements SensorMeasurementService
{
    public static final int SENSOR_THRESHOLD = 2000;
    SensorMeasurementRepository sensorMeasurementRepository;

    SensorAlertService sensorAlertService;

    SensorService sensorService;

    @Autowired
    public SensorMeasurementServiceImpl(
        SensorMeasurementRepository sensorMeasurementRepository, SensorAlertService sensorAlertService, SensorService sensorService)
    {
        this.sensorMeasurementRepository = sensorMeasurementRepository;
        this.sensorAlertService = sensorAlertService;
        this.sensorService = sensorService;
    }


    @Override
    @Transactional
    public void saveMeasurement(String uuid, SensorDTO sensorDTO)
    {
        Sensor sensor = sensorService.findByUuid(uuid);
        if (sensor == null)
        {
            sensor=saveSensor(uuid, sensorDTO);
        }
        else
        {
            List<SensorMeasurement> sensorMeasurements = sensorMeasurementRepository.getLastTwoSensorMeasurementBySensorId(sensor.getId());
            int sensorAlertCount = 0;
            int sensorOkCount = 0;
            if (sensorDTO.getCo2Level() > SENSOR_THRESHOLD)
                sensorAlertCount++;
            else
                sensorOkCount++;
            for (SensorMeasurement sensorMeasurement : sensorMeasurements)
            {
                if (sensorMeasurement.getCo2Level() > SENSOR_THRESHOLD)
                    sensorAlertCount++;
                else
                    sensorOkCount++;
            }

            sensor.setLastUpdatedDate(sensorDTO.getDate());
            setSensorStatus(sensorDTO, sensor, sensorAlertCount, sensorOkCount);
            sensorService.save(sensor);
            if (sensor.getSensorStatus() == SensorStatus.ALERT)
            {
                sensorAlertService.createAlert(sensor, sensorMeasurements, sensorDTO);
            }
        }
        SensorMeasurement sensorMeasurement = new SensorMeasurement();
        sensorMeasurement.setCo2Level(sensorDTO.getCo2Level());
        sensorMeasurement.setSensor(sensor);
        sensorMeasurement.setCreatedDate(sensorDTO.getDate());
        sensorMeasurementRepository.save(sensorMeasurement);
    }


    @Override
    public Map<String, String> getSensorMetrics(String uuid)
    {
        Map<String, String> map = new HashMap<>();
        List<Object[]> results = sensorMeasurementRepository.getSensorMetrics(uuid);
        if (results != null && results.get(0)[0]!=null)
        {
            map.put("avgLast30Days", results.get(0)[0].toString());
            map.put("maxLast30Days", results.get(0)[1].toString());
        }
        return map;
    }


    private void setSensorStatus(SensorDTO sensorDTO, Sensor sensor, int sensorAlertCount, int sensorOkCount)
    {
        if (sensorAlertCount == 3)
        {
            sensor.setSensorStatus(SensorStatus.ALERT);

        }
        else if (sensorOkCount == 3)
        {
            sensor.setSensorStatus(SensorStatus.OK);
        }

        else if (sensor.getSensorStatus() == SensorStatus.OK && sensorDTO.getCo2Level() > SENSOR_THRESHOLD)
        {
            sensor.setSensorStatus(SensorStatus.WARN);
        }
    }


    private Sensor saveSensor(String uuid, SensorDTO sensorDTO)
    {
        Sensor sensorNew = new Sensor();
        sensorNew.setUuid(uuid);
        sensorNew.setLastUpdatedDate(sensorDTO.getDate());
        if (sensorDTO.getCo2Level() > SENSOR_THRESHOLD)
        {
            sensorNew.setSensorStatus(SensorStatus.WARN);
        }
        return sensorService.save(sensorNew);
    }

}
