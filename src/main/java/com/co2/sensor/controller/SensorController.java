package com.co2.sensor.controller;

import com.co2.sensor.entity.Sensor;
import com.co2.sensor.enums.SensorStatus;
import com.co2.sensor.exception.InvalidUuidDException;
import com.co2.sensor.exception.RecordNotFoundException;
import com.co2.sensor.exception.SensorEmptyValueException;
import com.co2.sensor.model.SensorDTO;
import com.co2.sensor.service.SensorAlertService;
import com.co2.sensor.service.SensorMeasurementService;
import com.co2.sensor.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/sensors")
public class SensorController
{
    SensorService sensorService;
    SensorMeasurementService sensorMeasurementService;
    SensorAlertService sensorAlertService;


    public SensorController(SensorService sensorService, SensorMeasurementService sensorMeasurementService, SensorAlertService sensorAlertService)
    {
        this.sensorService = sensorService;
        this.sensorMeasurementService = sensorMeasurementService;
        this.sensorAlertService = sensorAlertService;
    }


    @Operation(summary = "Save sensor measurement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Measurement saved successfully.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid request supplied",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Exception occurred while saving measurement",
            content = @Content),
        @ApiResponse(responseCode = "503", description = "Service not available",
            content = @Content)})
    @PostMapping("/{uuid}/measurements")
    public ResponseEntity<Object> saveMeasurements(@PathVariable(value = "uuid") String uuid,@Valid @RequestBody SensorDTO sensorDTO)
        throws SensorEmptyValueException, InvalidUuidDException
    {
        if(sensorDTO.getCo2Level()==null || sensorDTO.getDate()==null){
            throw new SensorEmptyValueException("Sensor co2 level or date can not be blank");
        }
        validateUUID(uuid);
        sensorMeasurementService.saveMeasurement(uuid,sensorDTO);
        return new ResponseEntity<>("Measurement Saved successfully",HttpStatus.CREATED);
    }

   @Operation(summary = "get sensor status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sensor status",content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid request supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Sensor data not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Exception occurred while fetching sensor",content = @Content),
        @ApiResponse(responseCode = "503", description = "Service not available",content = @Content)})

    @GetMapping(path="/{uuid}",produces = "application/json")
    public ResponseEntity<Object> getSensorStatus(@PathVariable(value = "uuid") String uuid) throws RecordNotFoundException, InvalidUuidDException
    {
        validateUUID(uuid);
        Sensor sensor = sensorService.findByUuid(uuid);
        if (sensor == null) {
            throw new RecordNotFoundException("Sensor data Not Found");
        }
        Map<String,String> status=new HashMap<>();
        status.put("status",sensor.getSensorStatus().toString());
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @Operation(summary = "get sensor metrics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sensor metrics", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid request supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Sensor metrics not available", content = @Content),
        @ApiResponse(responseCode = "500", description = "Exception occurred while fetching sensor",content = @Content),
        @ApiResponse(responseCode = "503", description = "Service not available",content = @Content)})
    @GetMapping("/{uuid}/metrics")
    public ResponseEntity<Object> getSensorMetrics(@PathVariable(value = "uuid") String uuid) throws RecordNotFoundException, InvalidUuidDException
    {
        validateUUID(uuid);
        Map<String,String> metrics = sensorMeasurementService.getSensorMetrics(uuid);
        if (metrics == null) {
            throw new RecordNotFoundException("Sensor metrics not available");
        }

        return new ResponseEntity<>(metrics,HttpStatus.OK);
    }


    @Operation(summary = "get sensor alerts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sensor alert", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid request supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Sensor alerts not available", content = @Content),
        @ApiResponse(responseCode = "500", description = "Exception occurred while fetching sensor alerts",content = @Content),
        @ApiResponse(responseCode = "503", description = "Service not available",content = @Content)})
    @GetMapping("/{uuid}/alerts")
    public ResponseEntity<Object> getSensorAlerts(@PathVariable(value = "uuid") String uuid) throws RecordNotFoundException, InvalidUuidDException
    {
        validateUUID(uuid);
        List<Map<String,String>> alerts = sensorAlertService.getSensorAlerts(uuid);
        if (alerts == null) {
             throw new RecordNotFoundException("Alerts are not available");
        }

        return new ResponseEntity<>(alerts,HttpStatus.OK);
    }

    private void validateUUID(String uuid) throws InvalidUuidDException {
        try {
            UUID.fromString(uuid);
        } catch (Exception e) {
            throw new InvalidUuidDException("Invalid UUID");
        }

    }
}

