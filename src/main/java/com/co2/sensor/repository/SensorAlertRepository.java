package com.co2.sensor.repository;

import com.co2.sensor.entity.Sensor;
import com.co2.sensor.entity.SensorAlerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorAlertRepository extends JpaRepository<SensorAlerts,Long>
{
    @Query(value = "select sa.* from sensor_alerts sa,sensor s where sa.sensor_id = s.id and s.sensor_uuid=:uuid",nativeQuery = true)
    List<SensorAlerts> getAlerts(@Param("uuid") String uuid);
}
