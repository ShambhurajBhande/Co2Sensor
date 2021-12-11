package com.co2.sensor.repository;

import com.co2.sensor.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor,Long>
{
    @Query("Select s from Sensor s where s.uuid= :uuid")
    Sensor findByUUID(String uuid);
}
