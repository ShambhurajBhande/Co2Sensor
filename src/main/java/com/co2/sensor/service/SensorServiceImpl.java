package com.co2.sensor.service;

import com.co2.sensor.entity.Sensor;
import com.co2.sensor.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService
{

    SensorRepository sensorRepository;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository)
    {
        this.sensorRepository = sensorRepository;
    }


    @Override
    public Sensor findByUuid(String uuid)
    {
        return sensorRepository.findByUUID(uuid);
    }


    @Override
    public Sensor save(Sensor sensorNew)
    {
        return sensorRepository.save(sensorNew);
    }


}
