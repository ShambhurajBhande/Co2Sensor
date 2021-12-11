package com.co2.sensor.repository;

import com.co2.sensor.entity.SensorMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorMeasurementRepository extends JpaRepository<SensorMeasurement, Long>
{
    @Query(value = "select * from sensor_measurement where sensor_id = :sensor_id order by sensor_measurement_id desc limit 2", nativeQuery = true)
    List<SensorMeasurement> getLastTwoSensorMeasurementBySensorId(@Param("sensor_id") Long sensorId);

    @Query(value = "Select avg(sm.co2_level) as avgLast30Days,max(sm.co2_level) as maxLast30Days from sensor_measurement sm, sensor s where sm.sensor_id = s.id and created_date>curdate()-30 and s.sensor_uuid=:uuid", nativeQuery = true)
    List<Object[]> getSensorMetrics(@Param("uuid") String uuid);
}
